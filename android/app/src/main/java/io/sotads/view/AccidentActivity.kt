package io.sotads.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.transition.ChangeBounds
import androidx.transition.Slide
import androidx.transition.TransitionManager
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.sotads.R
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.Callback
import io.sotads.core.util.createNotificationChannel
import io.sotads.core.util.pushNotification
import io.sotads.data.Accident
import io.sotads.data.Driver
import io.sotads.databinding.ActivityAccidentBinding
import java.util.*

class AccidentActivity : BaseActivity() {
    private lateinit var binding: ActivityAccidentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accident)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        debugLog("Accident Key: ${intent.getStringExtra(ACCIDENT_KEY)}")
        debugLog("Driver Key: ${intent.getStringExtra(DRIVER_KEY)}")
        debugLog("Driver: ${intent.getStringExtra(DRIVER)}")

        if (intent.hasExtra(ACCIDENT_KEY)) {

            if (!intent.getStringExtra(ACCIDENT_KEY).isNullOrEmpty()) {
                firebase.getAccident(intent.getStringExtra(ACCIDENT_KEY), object : Callback<Accident> {
                    override fun onInit() {
                        toast("Loading driver\'s information...")
                    }

                    override fun onError(error: String?) {
                        debugLog(error)
                        toast(error, true)
                    }

                    override fun onSuccess(response: Accident?) {
                        if (response != null) {
                            binding.accident = response

                            try {
                                val location = binding.accident?.location
                                val geocoder = Geocoder(binding.root.context, Locale.getDefault())
                                binding.address.summary =
                                    geocoder.getFromLocation(
                                        location?.latitude ?: 5.623,
                                        location?.longitude ?: -0.184,
                                        1
                                    )[0].getAddressLine(0)
                            } catch (ex: Exception) {
                                debugLog(ex.localizedMessage)
                            }
                        }
                    }
                })
            }

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

//        createNotificationChannel(getString(R.string.notification_channel_name))
//        pushNotification("Demo notification", getString(R.string.lorem), Intent(this, HomeActivity::class.java))
    }

    companion object {
        const val ACCIDENT_KEY = "accident_key"
        const val DRIVER_KEY = "driver_key"
        const val DRIVER = "driver"
    }

    fun acceptDispatch(view: View) {
        firebase.acceptDispatch(binding.accident?.key, object : Callback<Void> {
            override fun onInit() {
                TransitionManager.beginDelayedTransition(binding.container, ChangeBounds())
                binding.loading.visibility = View.VISIBLE
                binding.dispatch.visibility = View.GONE
            }

            override fun onError(error: String?) {
                toast(error, true)
            }

            override fun onSuccess(response: Void?) {
                toast("Dispatch accepted. please attend to this driver immediately. Thank you", true)
                finishAfterTransition()
            }

            override fun onComplete() {
                TransitionManager.beginDelayedTransition(binding.container, Slide())
                binding.loading.visibility = View.GONE
                binding.dispatch.visibility = View.VISIBLE
            }
        })

    }
}