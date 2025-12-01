package org.saudigitus.e_prescription.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object Commons {
    fun triggerNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        title: String,
        message: String,
        icon: Int? = null,
        progress: Int? = null
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
        builder.apply {
            icon?.let {
                setSmallIcon(icon)
            }

            setContentTitle(title)
            setContentText(message)
            setAutoCancel(false)
            setOnlyAlertOnce(true)
            setOngoing(true)

            progress?.let {
                setProgress(100, progress, false)
            }

            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notificationManager.notify(notificationId, builder.build())
    }


    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
    }


}