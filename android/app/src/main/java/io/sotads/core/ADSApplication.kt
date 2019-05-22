package io.sotads.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import io.codelabs.sdk.util.debugLog
import io.sotads.core.injection.firebaseModule
import io.sotads.core.injection.roomModule
import io.sotads.core.util.PREFS_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ADSApplication : Application() {
    lateinit var prefs: SharedPreferences
    private val job = Job()
    val ioScope = CoroutineScope(Dispatchers.IO + job)
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

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

    override fun onTerminate() {
        job.cancel()
        super.onTerminate()
    }
}