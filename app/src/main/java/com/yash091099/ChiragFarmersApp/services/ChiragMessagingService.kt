package com.yash091099.ChiragFarmersApp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import timber.log.Timber
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yash091099.ChiragFarmersApp.R

class ChiragMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Message received from: ${remoteMessage.from}")

        val data = remoteMessage.data
        val notificationPayload = remoteMessage.notification

        val url = if (data["type"] == "app_update") data["url"] else null

        if (notificationPayload != null) {
            Timber.d("Message notification title: ${notificationPayload.title}")
            Timber.d("Message notification body: ${notificationPayload.body}")

            sendNotification(
                title = notificationPayload.title ?: "ChiragFarmer",
                body = notificationPayload.body ?: "",
                url = url
            )
        } else if (data.isNotEmpty()) {
            Timber.d("Message data payload: $data")

            val title = data["title"] ?: "ChiragFarmer"
            val body = data["body"] ?: getString(R.string.notification_new_message)

            sendNotification(title = title, body = body, url = url)
        }
    }

    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        // Note: The token sync is handled automatically when the app opens via HomeViewModel
        // This callback is called when FCM generates a new token
    }

    private fun sendNotification(title: String, body: String, url: String? = null) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.notification_channel_description)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        val contentIntent = if (!url.isNullOrBlank()) {
            PendingIntent.getActivity(
                this,
                0,
                Intent(Intent.ACTION_VIEW, Uri.parse(url)),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else null

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.chirag_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        contentIntent?.let { notificationBuilder.setContentIntent(it) }

        val notificationId = (System.currentTimeMillis() / 1000).toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())

        Timber.d("Notification shown with ID: $notificationId" + if (url != null) " URL: $url" else "")
    }

    companion object {
        private const val TAG = "ChiragMessagingService"
        private const val CHANNEL_ID = "chirag_farmer_notifications"
    }
}



