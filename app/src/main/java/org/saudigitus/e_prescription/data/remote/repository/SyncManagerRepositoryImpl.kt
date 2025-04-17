package org.saudigitus.e_prescription.data.remote.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import io.reactivex.Completable
import org.hisp.dhis.android.core.D2
import org.hisp.dhis.android.core.common.State
import org.hisp.dhis.android.core.fileresource.FileResourceElementType
import org.hisp.dhis.android.core.fileresource.FileResourceValueType
import org.hisp.dhis.android.core.settings.SynchronizationSettings
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.model.SyncResult
import org.saudigitus.e_prescription.data.model.WorkItem
import org.saudigitus.e_prescription.data.model.WorkType
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.data.remote.WorkManagerRepository
import org.saudigitus.e_prescription.utils.Constants
import org.saudigitus.e_prescription.utils.Constants.INITIAL_SYNC
import org.saudigitus.e_prescription.utils.Constants.INSTANT_DATA_SYNC
import org.saudigitus.e_prescription.utils.Constants.INSTANT_METADATA_SYNC
import org.saudigitus.e_prescription.utils.UIDMapping
import org.saudigitus.e_prescription.utils.toSeconds
import javax.inject.Inject

class SyncManagerRepositoryImpl
@Inject constructor(
    private val d2: D2,
    private val workManagerRepository: WorkManagerRepository,
    private val preferenceProvider: PreferenceProvider
): SyncManagerRepository {

    override fun sync() {
        workManagerRepository.sync(INITIAL_SYNC, INSTANT_METADATA_SYNC, INSTANT_DATA_SYNC)
    }

    override fun syncMetadata() {
        Completable.fromObservable(d2.metadataModule().download()).blockingAwait()
    }

    override fun syncData() {
        Log.d("SYNCING","Syncing data")
        upload().andThen(
            Completable.fromObservable(
                d2.trackedEntityModule().trackedEntityInstanceDownloader()
                    .byProgramUid(UIDMapping.PROGRAM)
                    .limitByOrgunit(true)
                    .limitByProgram(true)
                    .download()
            ).andThen(
                Completable.fromObservable(
                    d2.fileResourceModule().fileResourceDownloader()
                        .byProgramUid().eq(UIDMapping.PROGRAM)
                        .byValueType().`in`(listOf(FileResourceValueType.IMAGE, FileResourceValueType.FILE_RESOURCE))
                        .byElementType().eq(FileResourceElementType.DATA_ELEMENT)
                        .download()
                )
            )
        ).blockingAwait()
        Log.d("SYNCING","End Syncing data")

    }

    override fun syncDataWithTrigger() {
        workManagerRepository.syncData(Constants.INITIAL_SYNC, INSTANT_DATA_SYNC)
    }

    override fun syncMetaDataWithTrigger() {
        workManagerRepository.syncMetaData(Constants.INITIAL_SYNC, INSTANT_METADATA_SYNC)
    }

    override fun syncEvents() {
        Completable.fromObservable(
            d2.eventModule().eventDownloader().download()
        ).blockingAwait()
    }

    override fun upload(): Completable {
        return Completable.fromObservable(
            d2.trackedEntityModule().trackedEntityInstances()
                .upload().concatWith(d2.eventModule().events().upload())
        )
    }

    override fun checkSyncStatus(): SyncResult {
        val teisSynced = d2.trackedEntityModule()
            .trackedEntityInstances()
            .byAggregatedSyncState()
            .notIn(State.SYNCED, State.RELATIONSHIP)
            .blockingGet()
            .isEmpty()

        if (teisSynced)
            return SyncResult.SYNCED

        val outstandingTEIsToPostOrUpdate = d2.trackedEntityModule()
            .trackedEntityInstances()
            .byAggregatedSyncState().
            `in`(State.TO_POST, State.TO_UPDATE)
            .blockingGet().isNotEmpty()

        if (outstandingTEIsToPostOrUpdate)
            return SyncResult.INCOMPLETE

        return SyncResult.ERROR
    }

    override fun schedulePeriodicDataSync() {
        val scheduledTimeInSecs = getSyncSettings()?.dataSync()?.toSeconds()
            ?: preferenceProvider.getInt(Constants.SYNC_PERIOD_DATA, Constants.PERIOD_DAILY)

        workManagerRepository.cancelUniqueWork(Constants.SCHEDULED_DATA_SYNC)

        if (scheduledTimeInSecs != Constants.PERIOD_MANUAL) {
            val work = WorkItem(
                Constants.SCHEDULED_DATA_SYNC,
                WorkType.DATA,
                null,
                scheduledTimeInSecs.toLong(),
                policy = ExistingWorkPolicy.REPLACE
            )

            workManagerRepository.sync(work)
        }
    }

    override fun schedulePeriodicMetadataSync() {
        val scheduledTimeInSecs = getSyncSettings()?.metadataSync()?.toSeconds()
            ?: preferenceProvider.getInt(Constants.SYNC_PERIOD_METADATA, Constants.PERIOD_DAILY)

        workManagerRepository.cancelUniqueWork(Constants.SCHEDULED_METADATA_SYNC)

        if (scheduledTimeInSecs != Constants.PERIOD_MANUAL) {
            val work = WorkItem(
                Constants.SCHEDULED_METADATA_SYNC,
                WorkType.METADATA,
                null,
                scheduledTimeInSecs.toLong(),
                policy = ExistingWorkPolicy.REPLACE
            )

            workManagerRepository.sync(work)
        }
    }

    override fun getSyncStatus(workName: String): LiveData<List<WorkInfo>> {
        return workManagerRepository.getWorkInfo(workName)
    }

    override fun getSyncSettings(): SynchronizationSettings? {
        return d2.settingModule().synchronizationSettings().blockingGet()
    }
}