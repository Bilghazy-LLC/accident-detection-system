package io.sotads.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.sotads.R
import io.sotads.core.ADSApplication
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.Callback
import io.sotads.data.Accident
import io.sotads.databinding.ActivityHomeBinding
import io.sotads.view.recyclerview.AccidentAdapter
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: AccidentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.shimmerContainer.startShimmer()

        // Setup recyclerview
        adapter = AccidentAdapter(this)
        binding.grid.adapter = adapter
        val lm = LinearLayoutManager(this)
        binding.grid.layoutManager = lm
        binding.grid.addItemDecoration(/*DividerItemDecoration(this, lm.orientation)*/ GridItemDividerDecoration(this,R.dimen.divider_height,R.color.divider))
        binding.grid.setHasFixedSize(true)

        firebase.getAccidents(object : Callback<MutableList<Accident>> {
            override fun onError(error: String?) {
                binding.shimmerContainer.stopShimmer()
                toast(error, true)
            }

            override fun onSuccess(response: MutableList<Accident>?) {
                if (response != null) {
                    debugLog(response)
                    adapter.addItems(response)
                }
            }

            override fun onComplete() {
                TransitionManager.beginDelayedTransition(binding.container)
                binding.shimmerContainer.visibility = View.GONE
                binding.grid.visibility = View.VISIBLE
            }
        })

    }

    override fun onEnterAnimationComplete() {
        (application as ADSApplication).ioScope.launch {
            firebase.subscribeToTopic()
        }
    }

}