package com.yash091099.ChiragFarmersApp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import timber.log.Timber
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yash091099.ChiragFarmersApp.R

class ChiragMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Message received from: ${remoteMessage.from}")

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Timber.d("Message notification title: ${it.title}")
            Timber.d("Message notification body: ${it.body}")

            // Send notification to user
            sendNotification(
                title = it.title ?: "ChiragFarmer",
                body = it.body ?: ""
            )
        }

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")

            // Handle data payload here if needed
            val title = remoteMessage.data["title"] ?: "ChiragFarmer"
            val body = remoteMessage.data["body"] ?: getString(R.string.notification_new_message)

            // If there was no notification payload but there's data payload, show notification
            if (remoteMessage.notification == null) {
                sendNotification(title = title, body = body)
            }
        }
    }

    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        // Note: The token sync is handled automatically when the app opens via HomeViewModel
        // This callback is called when FCM generates a new token
    }

    private fun sendNotification(title: String, body: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create notification channel
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.notification_channel_description)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.chirag_logo)  // Replace with your app icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        // Show the notification
        val notificationId = (System.currentTimeMillis() / 1000).toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())

        Timber.d("Notification shown with ID: $notificationId")
    }

    companion object {
        private const val TAG = "ChiragMessagingService"
        private const val CHANNEL_ID = "chirag_farmer_notifications"
    }
}



