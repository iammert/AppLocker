package com.momentolabs.app.security.applocker.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.ui.main.MainActivity
import javax.inject.Inject

class ServiceNotificationManager @Inject constructor(val context: Context) {

    fun createNotification(): Notification {
        createAppLockerServiceChannel()

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_APPLOCKER_SERVICE)
            .setSmallIcon(R.drawable.ic_round_lock_24px)
            .setContentTitle(context.getString(R.string.notification_protecting_title))
            .setContentText(context.getString(R.string.notification_protecting_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_APPLOCKER_SERVICE, notification)
        return notification
    }

    fun createPermissionNeedNotification(): Notification {
        createAppLockerServiceChannel()

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_APPLOCKER_SERVICE)
            .setSmallIcon(R.drawable.ic_round_lock_24px)
            .setContentTitle(context.getString(R.string.notification_permission_need_title))
            .setContentText(context.getString(R.string.notification_permission_need_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_APPLOCKER_PERMISSION_NEED, notification)
        return notification
    }

    fun hidePermissionNotification(){
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID_APPLOCKER_PERMISSION_NEED)
    }

    private fun createAppLockerServiceChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID_APPLOCKER_SERVICE, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID_APPLOCKER_SERVICE = "CHANNEL_ID_APPLOCKER_SERVICE"
        private const val NOTIFICATION_ID_APPLOCKER_SERVICE = 1
        private const val NOTIFICATION_ID_APPLOCKER_PERMISSION_NEED = 2
    }
}