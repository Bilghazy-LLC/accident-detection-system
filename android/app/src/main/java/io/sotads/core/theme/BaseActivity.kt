package io.sotads.core.theme

import io.codelabs.sdk.view.BaseActivity
import io.sotads.core.firebase.Firebase
import io.sotads.core.room.ADSDao
import org.koin.android.ext.android.inject

abstract class BaseActivity : BaseActivity() {
    val dao: ADSDao by inject()
    val firebase: Firebase by inject()

}