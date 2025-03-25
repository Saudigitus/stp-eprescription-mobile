package org.saudigitus.e_prescription.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.local.PreferenceProvider
import org.saudigitus.e_prescription.data.model.SyncResult
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.utils.Commons
import org.saudigitus.e_prescription.utils.Constants
import org.saudigitus.e_prescription.utils.DateUtils
import org.saudigitus.e_prescription.utils.ResourceManager
import java.time.LocalDateTime

@Suppress("SameParameterValue")
@HiltWorker
class AppDataSyncWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncManager: SyncManagerRepository,
    private val preferenceProvider: PreferenceProvider,
    private val resourceManager: ResourceManager
) : Worker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        var teiSynced = false
        var errorPayload: Data? = null

        triggerNotification(
            R.string.app_name,
            R.string.data_sync_in_progress,
            R.drawable.ic_sync
        )

        try {
            syncManager.syncData()
            teiSynced = true
        } catch (e: Exception) {
            e.printStackTrace()

            resourceManager.getD2ErrorMessage(e)?.let {
                errorPayload = Data.Builder().putInt(Constants.WORKER_ERROR_MESSAGE_KEY, it).build()
            }
        }

        triggerNotification(
            R.string.app_name,
            if (teiSynced) R.string.sync_completed else R.string.data_sync_error,
            if (teiSynced) R.drawable.ic_status_synced
            else R.drawable.ic_sync_warning
        )

        val syncDate = LocalDateTime.now().format(DateUtils.getDateTimePattern())
        preferenceProvider.setValue(Constants.LAST_DATA_SYNC_DATE, syncDate)
        preferenceProvider.setValue(Constants.LAST_DATA_SYNC_STATUS, teiSynced)

        val syncStatus: SyncResult = if (teiSynced) {
            syncManager.checkSyncStatus()
        } else { SyncResult.ERROR }

        preferenceProvider.setValue(Constants.LAST_DATA_SYNC_RESULT, syncStatus.name)

        cancelNotification(Constants.SYNC_DATA_NOTIFICATION_ID)
        syncManager.schedulePeriodicDataSync()

        return if (teiSynced) {
            Result.success()
        } else {
            errorPayload?.let {
                Result.failure(it)
            } ?: Result.failure()
        }
    }

    private fun triggerNotification(title: Int, message: Int, icon: Int?) {
        Commons.triggerNotification(
            applicationContext,
            Constants.SYNC_DATA_NOTIFICATION_ID,
            Constants.SYNC_DATA_NOTIFICATION_CHANNEL,
            Constants.SYNC_DATA_CHANNEL_NAME,
            applicationContext.getString(title),
            applicationContext.getString(message),
            icon
        )
    }

    private fun cancelNotification(notificationId: Int) {
        Commons.cancelNotification(applicationContext, notificationId)
    }
}