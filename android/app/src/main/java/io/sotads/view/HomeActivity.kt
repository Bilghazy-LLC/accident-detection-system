package io.sotads.view

import android.os.Bundle
import io.sotads.R
import io.sotads.core.ADSApplication
import io.sotads.core.theme.BaseActivity
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


    }

    override fun onEnterAnimationComplete() {
        (application as ADSApplication).ioScope.launch {
            firebase.subscribeToTopic()
        }
    }

}