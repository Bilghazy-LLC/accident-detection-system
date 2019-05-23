package io.sotads.core.firebase

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.sdk.util.debugLog
import io.sotads.R
import io.sotads.core.util.PREFS_NAME
import io.sotads.core.util.USER_KEY
import io.sotads.core.util.createNotificationChannel
import io.sotads.core.util.pushNotification
import io.sotads.view.AccidentActivity

class ADSMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(rm: RemoteMessage?) {
        if (rm != null && rm.data != null) {
            val data = rm.data

            createNotificationChannel(data["title"] ?: getString(R.string.notification_channel_name))

            if (!applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(
                    USER_KEY,
                    null
                ).isNullOrEmpty()
            ) {
                pushNotification(
                    data["title"],
                    data["Tap here for more details"],
                    Intent(applicationContext, AccidentActivity::class.java).apply {
                        putExtra(AccidentActivity.ACCIDENT_KEY, data["key"])
                    }
                )
            }
        }
    }


    override fun onNewToken(newToken: String?) {
        debugLog("New Token retrieved: $newToken")
    }
}