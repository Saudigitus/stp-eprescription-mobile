package org.saudigitus.e_prescription.data.remote.repository


import androidx.lifecycle.LiveData
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import org.saudigitus.e_prescription.data.model.WorkItem
import org.saudigitus.e_prescription.data.model.WorkType
import org.saudigitus.e_prescription.data.remote.WorkManagerRepository
import org.saudigitus.e_prescription.service.AppDataSyncWorker
import org.saudigitus.e_prescription.service.AppMetadataSyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerRepositoryImpl
@Inject constructor(
    private val workManager: WorkManager
): WorkManagerRepository {
    override fun sync(workName: String, metadataTag: String, dataTag: String) {
        val syncMetadataBuilder = OneTimeWorkRequest.Builder(AppMetadataSyncWorker::class.java)
        syncMetadataBuilder.addTag(metadataTag)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )

        val syncDataBuilder = OneTimeWorkRequest.Builder(AppDataSyncWorker::class.java)
        syncDataBuilder.addTag(dataTag)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )

        workManager.beginUniqueWork(workName, ExistingWorkPolicy.KEEP, syncMetadataBuilder.build())
            .then(syncDataBuilder.build())
            .enqueue()
    }

    override fun sync(workItem: WorkItem) {
        val builder = createOneTimeBuilder(workItem).build()
        workItem.policy?.let { workManager.enqueueUniqueWork(workItem.name, it, builder) }
            ?: run { workManager.enqueue(builder) }
    }

    override fun syncData(workName: String, dataTag: String) {
        val syncDataBuilder = OneTimeWorkRequest.Builder(AppDataSyncWorker::class.java)
        syncDataBuilder.addTag(dataTag)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )

        workManager.beginUniqueWork(workName, ExistingWorkPolicy.KEEP, syncDataBuilder.build())
            .enqueue()
    }

    override fun getWorkInfo(workName: String): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosForUniqueWorkLiveData(workName)
    }

    override fun getWorkInfoByTag(tag: String): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosByTagLiveData(tag)
    }

    override fun cancelUniqueWork(workName: String) {
        workManager.cancelUniqueWork(workName)
    }

    override fun cancelWorkByTag(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    override fun cancelAllWork() {
        workManager.cancelAllWork()
    }

    private fun createOneTimeBuilder(workItem: WorkItem): OneTimeWorkRequest.Builder {
        val builder = when (workItem.type) {
            WorkType.METADATA -> OneTimeWorkRequest.Builder(AppMetadataSyncWorker::class.java)
            WorkType.DATA -> OneTimeWorkRequest.Builder(AppDataSyncWorker::class.java)
        }

        builder.apply {
            addTag(workItem.name)
            setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
        }

        workItem.delayInSecs?.let { builder.setInitialDelay(it, TimeUnit.SECONDS) }
        workItem.data?.let { builder.setInputData(it) }

        return builder
    }

    private fun createPeriodicBuilder(workItem: WorkItem): PeriodicWorkRequest.Builder {
        val seconds = workItem.delayInSecs ?: 0

        val builder = when (workItem.type) {
            WorkType.METADATA -> {
                PeriodicWorkRequest.Builder(
                    AppMetadataSyncWorker::class.java,
                    seconds,
                    TimeUnit.SECONDS
                )
            }
            WorkType.DATA -> {
                PeriodicWorkRequest.Builder(
                    AppDataSyncWorker::class.java,
                    seconds,
                    TimeUnit.SECONDS
                )
            }
        }

        builder.apply {
            addTag(workItem.name)
            setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
        }

        workItem.data?.let { builder.setInputData(it) }

        return builder
    }
}