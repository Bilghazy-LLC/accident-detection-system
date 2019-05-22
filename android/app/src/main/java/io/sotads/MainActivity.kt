package io.sotads

import android.os.Bundle
import android.view.View
import io.sotads.core.theme.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun authenticateUser(view: View) {

    }
}
