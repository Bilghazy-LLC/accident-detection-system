package io.sotads.core.injection

import io.sotads.core.room.ADSDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single { ADSDatabase.get(androidContext()).dao() }
}

val firebaseModule = module {

}