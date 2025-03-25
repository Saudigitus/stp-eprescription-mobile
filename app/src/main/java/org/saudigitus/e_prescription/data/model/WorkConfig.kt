package org.saudigitus.e_prescription.data.model

import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy

enum class WorkType {
    DATA,
    METADATA
}

data class WorkItem(
    val name: String,
    val type: WorkType,
    val data: Data? = null,
    val delayInSecs: Long? = null,
    val policy: ExistingWorkPolicy? = null,
    val periodicWorkPolicy: ExistingPeriodicWorkPolicy? = null
)