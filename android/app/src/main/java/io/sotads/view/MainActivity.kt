package io.sotads.view

import android.os.Bundle
import android.view.View
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
            val snackbar = Snackbar.make(container, "", Snackbar.LENGTH_INDEFINITE)
            firebase.login(this, dao, object : Callback<EmtDataModel> {
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
            })
        } else {
            intentTo(HomeActivity::class.java, true)
        }
    }
}
