package org.saudigitus.e_prescription.data.remote


import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import org.saudigitus.e_prescription.data.model.WorkItem

interface WorkManagerRepository {
    fun sync(workName: String, metadataTag: String, dataTag: String)
    fun sync(workItem: WorkItem)
    fun syncData(workName: String, dataTag: String)

    fun getWorkInfo(workName: String): LiveData<List<WorkInfo>>
    fun getWorkInfoByTag(tag: String): LiveData<List<WorkInfo>>

    fun cancelUniqueWork(workName: String)
    fun cancelWorkByTag(tag: String)
    fun cancelAllWork()
}