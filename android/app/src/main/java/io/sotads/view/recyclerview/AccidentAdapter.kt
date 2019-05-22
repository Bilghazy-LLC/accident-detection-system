package io.sotads.view.recyclerview

import android.content.Intent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.sdk.util.debugLog
import io.sotads.R
import io.sotads.core.theme.BaseActivity
import io.sotads.core.util.Callback
import io.sotads.data.Accident
import io.sotads.data.Driver
import io.sotads.view.AccidentActivity
import kotlinx.coroutines.launch

class AccidentAdapter constructor(private val ctx: BaseActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_EMPTY = R.layout.item_empty
        private const val TYPE_ITEM = R.layout.item_accident
    }

    private val dataSource = mutableListOf<Accident>()

    override fun getItemViewType(position: Int): Int = if (dataSource.isEmpty()) TYPE_EMPTY else TYPE_ITEM


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            TYPE_ITEM -> ItemViewHolder(DataBindingUtil.inflate(ctx.layoutInflater, TYPE_ITEM, parent, false))

            TYPE_EMPTY -> EmptyViewHolder(ctx.layoutInflater.inflate(TYPE_EMPTY, parent, false))

            else -> throw IllegalArgumentException("Illegal viewholder detected")
        }
    }

    override fun getItemCount(): Int = if (dataSource.isEmpty()) 1 else dataSource.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ITEM -> bindItemViewHolder(holder as ItemViewHolder, dataSource[position])

            TYPE_EMPTY -> bindEmptyViewHolder(holder as EmptyViewHolder)
        }
    }

    fun addItems(items: MutableList<Accident>) {
        dataSource.clear()
        dataSource.addAll(items)
        notifyDataSetChanged()
    }

    private fun bindEmptyViewHolder(holder: EmptyViewHolder) {}

    private fun bindItemViewHolder(holder: ItemViewHolder, accident: Accident) {
        ctx.firebase.getDriver(accident.driver?.id!!, object : Callback<Driver> {
            override fun onError(error: String?) {
                debugLog(error)
            }

            override fun onSuccess(response: Driver?) {
                if (response != null) {
                    holder.bind(accident, response)
                }
            }
        })

        holder.binding.root.setOnClickListener {
            performIntent((holder.binding.accident as? Accident)?.key, holder.binding.driver as? Driver)
        }

        holder.binding.viewDetails.setOnClickListener {
            performIntent((holder.binding.accident as? Accident)?.key, holder.binding.driver as? Driver)
        }

        ctx.ioScope.launch {
            val address = holder.getAddress()

            ctx.uiScope.launch {
                holder.binding.address.summary = address
            }
        }

    }

    private fun performIntent(accident: String?, driver: Driver?) {
        val intent = Intent(ctx, AccidentActivity::class.java).apply {
            putExtra(AccidentActivity.ACCIDENT_KEY, accident)
            putExtra(AccidentActivity.DRIVER, driver)
            putExtra(AccidentActivity.DRIVER_KEY, driver?.key)
        }
        ctx.startActivity(intent)
    }
}