package io.sotads.view

import android.os.Bundle
import io.codelabs.sdk.util.debugLog
import io.sotads.R
import io.sotads.core.theme.BaseActivity

class AccidentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        debugLog("Accident Key: ${intent.getStringExtra(ACCIDENT_KEY)}")
    }

    companion object {
        const val ACCIDENT_KEY = "accident_key"
    }
}