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
import org.saudigitus.e_prescription.data.remote.SyncManagerRepository
import org.saudigitus.e_prescription.utils.Commons
import org.saudigitus.e_prescription.utils.Constants
import org.saudigitus.e_prescription.utils.DateUtils
import org.saudigitus.e_prescription.utils.ResourceManager
import java.time.LocalDateTime

@HiltWorker
class AppMetadataSyncWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workParams: WorkerParameters,
    private val syncManager: SyncManagerRepository,
    private val preferenceProvider: PreferenceProvider,
    private val resourceManager: ResourceManager
): Worker(context, workParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        var metadataSynced = false
        var errorPayload: Data? = null

        triggerNotification(
            R.string.app_name,
            R.string.metadata_sync_in_progress,
            R.drawable.ic_sync
        )

        try {
            syncManager.syncMetadata()
            metadataSynced = true
        } catch (e: Exception) {
            e.printStackTrace()

            resourceManager.getD2ErrorMessage(e)?.let {
                errorPayload = Data.Builder().putInt(Constants.WORKER_ERROR_MESSAGE_KEY, it).build()
            }
        }

        triggerNotification(
            R.string.app_name,
            if (metadataSynced) R.string.metadata_sync_completed
            else R.string.metadata_sync_error,
            if (metadataSynced) R.drawable.ic_status_synced
            else R.drawable.ic_sync_warning
        )

        val syncDate = LocalDateTime.now().format(DateUtils.getDateTimePattern())
        preferenceProvider.setValue(Constants.LAST_METADATA_SYNC_DATE, syncDate)
        preferenceProvider.setValue(Constants.LAST_METADATA_SYNC_STATUS, metadataSynced)

        cancelNotification()

        syncManager.schedulePeriodicMetadataSync()

        return if (metadataSynced) {
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
            Constants.SYNC_METADATA_NOTIFICATION_ID,
            Constants.SYNC_METADATA_NOTIFICATION_CHANNEL,
            Constants.SYNC_METADATA_CHANNEL_NAME,
            applicationContext.getString(title),
            applicationContext.getString(message),
            icon
        )
    }

    private fun cancelNotification() {
        Commons.cancelNotification(applicationContext, Constants.SYNC_METADATA_NOTIFICATION_ID)
    }
}