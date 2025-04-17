package org.saudigitus.e_prescription.data.remote

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import io.reactivex.Completable
import org.hisp.dhis.android.core.settings.SynchronizationSettings
import org.saudigitus.e_prescription.data.model.SyncResult

interface SyncManagerRepository {

    fun sync()
    fun syncMetadata()
    fun syncData()
    fun syncDataWithTrigger()
    fun syncMetaDataWithTrigger()
    fun syncEvents()
    fun upload(): Completable
    fun checkSyncStatus(): SyncResult
    fun schedulePeriodicDataSync()
    fun schedulePeriodicMetadataSync()
    fun getSyncStatus(workName: String): LiveData<List<WorkInfo>>
    fun getSyncSettings(): SynchronizationSettings?
}