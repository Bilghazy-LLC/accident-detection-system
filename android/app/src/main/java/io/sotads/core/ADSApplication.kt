package io.sotads.core

import android.app.Application
import com.google.firebase.FirebaseApp
import io.codelabs.sdk.util.debugLog
import io.sotads.core.injection.firebaseModule
import io.sotads.core.injection.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ADSApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        // Initialize Firebase SDK
        FirebaseApp.initializeApp(this).also {
            debugLog("Firebase SDK: ${it?.name}")
        }

        // Initialize Koin DSL
        startKoin {
            androidContext(this@ADSApplication)
            modules(roomModule, firebaseModule)
        }
    }
}