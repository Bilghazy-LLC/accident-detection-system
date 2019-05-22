package io.sotads.core.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.sdk.util.debugLog

class ADSMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(rm: RemoteMessage?) {
        if (rm != null && rm.data != null) {
            val data = rm.data



        }
    }


    override fun onNewToken(newToken: String?) {
        debugLog("New Token retrieved: $newToken")
    }
}