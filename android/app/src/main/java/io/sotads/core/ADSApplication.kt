package io.sotads.core

import android.app.Application
import io.sotads.core.injection.firebaseModule
import io.sotads.core.injection.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ADSApplication : Application() {

    override fun onCreate() {
        super.onCreate()




        startKoin {
            androidContext(this@ADSApplication)
            modules(roomModule, firebaseModule)
        }
    }
}