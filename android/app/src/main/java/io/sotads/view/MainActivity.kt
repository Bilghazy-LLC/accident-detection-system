package io.sotads.view

import android.os.Bundle
import android.view.View
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.sotads.R
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.Callback
import io.sotads.core.util.USER_KEY
import io.sotads.data.EmtDataModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun authenticateUser(view: View) {
        if (prefs.getString(USER_KEY, null).isNullOrEmpty()) {
            val snackbar = Snackbar.make(container, "Logging in...", Snackbar.LENGTH_INDEFINITE)
            firebase.login(this, dao, object : Callback<EmtDataModel> {
                override fun onInit() {
                    snackbar.show()
                    TransitionManager.beginDelayedTransition(container, Slide())
                    loading.visibility = View.VISIBLE
                    login_button.visibility = View.GONE
                }

                override fun onError(error: String?) {
                    snackbar.apply {
                        setText(error ?: "An error occurred while logging you in")
                        duration = BaseTransientBottomBar.LENGTH_LONG
                        show()
                    }
                }

                override fun onSuccess(response: EmtDataModel?) {
                    debugLog("Logged in user: $response")
                }

                override fun onComplete() {
                    TransitionManager.beginDelayedTransition(container, Slide())
                    loading.visibility = View.GONE
                    login_button.visibility = View.VISIBLE
                }
            })
        } else {
            intentTo(HomeActivity::class.java, true)
        }
    }
}
