package io.sotads.core.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import io.sotads.R
import io.sotads.core.firebase.ADSMessagingService

// Misc
const val ROOM_DB_NAME = "ads_room.db"
const val PREFS_NAME = "ads_prefs"
const val USER_KEY = "user_key"

// Messaging
const val TOPIC_NAME = "emt_dispatch"

// Database
const val EMT_REF = "emt"
const val ACCIDENTS_REF = "accidents"
const val DRIVERS_REF = "drivers"


fun Context.pushNotification(title: String?, content: String?, intent: Intent) {
    // Create a notification instance
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    //Create pending intent for activity
    val pi = PendingIntent.getActivity(
        applicationContext,
        125,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //Create notification builder
    val builder = NotificationCompat.Builder(this, getString(R.string.notification_channel_name))
        .setSmallIcon(R.drawable.shr_logo)
        .setContentTitle(title)
        .setContentText(content)
        .setContentIntent(pi)
        .setStyle(
            if (content != null && content.startsWith("https")) NotificationCompat.BigPictureStyle()
            else NotificationCompat.BigTextStyle()
        )
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        .setVibrate(longArrayOf(0, 200, 0, 300))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    //Send notification
    manager?.notify(125, builder.build())

    //Wake the screen when the device is locked
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(
        PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.PARTIAL_WAKE_LOCK,
        ADSMessagingService::class.java.canonicalName
    )
    wakeLock.acquire(15000)
}

fun Context.createNotificationChannel(channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val descriptionText = getString(R.string.notification_channel_name)
        val channel =
            NotificationChannel(
                getString(R.string.notification_channel_name),
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}