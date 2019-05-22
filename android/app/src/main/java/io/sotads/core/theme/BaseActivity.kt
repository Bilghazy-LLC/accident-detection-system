package io.sotads.core.theme

import android.content.SharedPreferences
import android.os.Bundle
import io.codelabs.sdk.view.BaseActivity
import io.sotads.core.ADSApplication
import io.sotads.core.firebase.Firebase
import io.sotads.core.room.ADSDao
import org.koin.android.ext.android.inject

abstract class BaseActivity : BaseActivity() {
    val dao: ADSDao by inject()
    val firebase: Firebase by inject()
    lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = (application as ADSApplication).prefs
    }
}