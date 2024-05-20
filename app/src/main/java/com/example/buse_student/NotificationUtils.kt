package com.example.buse_student

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {

    private const val CHANNEL_ID = "example_channel"
    private const val CHANNEL_NAME = "Example Channel"
    private const val CHANNEL_DESCRIPTION = "This is an example channel"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(false)
                lightColor = Color.TRANSPARENT
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build()) // 1 is the notification ID, which you can use to update or cancel the notification
            }

        }
    }

    fun updateNotificationContent(context: Context, title: String, newContent: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background) // Make sure this icon exists in your resources
            .setContentTitle(title) // You might want to keep the title static or change it accordingly
            .setContentText(newContent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSound(null) // Explicitly set sound to null
            .setVibrate(longArrayOf(0L))

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build()) // Use the same notification ID to update the existing notification
            }

        }
    }

}