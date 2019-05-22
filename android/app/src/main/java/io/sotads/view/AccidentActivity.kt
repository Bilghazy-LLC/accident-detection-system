package io.sotads.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.sotads.R
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.Callback
import io.sotads.data.Driver
import io.sotads.databinding.ActivityAccidentBinding

class AccidentActivity : BaseActivity() {
    private lateinit var binding: ActivityAccidentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accident)

        debugLog("Accident Key: ${intent.getStringExtra(ACCIDENT_KEY)}")
        debugLog("Driver Key: ${intent.getStringExtra(DRIVER_KEY)}")
        debugLog("Driver: ${intent.getStringExtra(DRIVER)}")

        if (intent.hasExtra(ACCIDENT_KEY)) {

            // Bind driver information
            if (intent.hasExtra(DRIVER)) {
                binding.driver = intent.getParcelableExtra(DRIVER)
            } else if (intent.hasExtra(DRIVER_KEY)) {
                firebase.getDriver(intent.getStringExtra(DRIVER_KEY), object : Callback<Driver> {
                    override fun onInit() {
                        toast("Loading driver\'s information...")
                    }

                    override fun onError(error: String?) {
                        debugLog(error)
                        toast(error, true)
                    }

                    override fun onSuccess(response: Driver?) {
                        if (response != null) binding.driver = response
                    }
                })
            }
        }
    }

    companion object {
        const val ACCIDENT_KEY = "accident_key"
        const val DRIVER_KEY = "driver_key"
        const val DRIVER = "driver"
    }

    fun acceptDispatch(view: View) {}
}