package io.sotads.view.recyclerview

import android.location.Geocoder
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.sdk.util.debugLog
import io.sotads.data.Accident
import io.sotads.data.Driver
import io.sotads.databinding.ItemAccidentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ItemViewHolder(val binding: ItemAccidentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(accident: Accident, driver: Driver) {
        binding.accident = accident
        binding.driver = driver
    }

    suspend fun getAddress(): String {
        var addressName = ""
        withContext(Dispatchers.IO) {
            try {
                val location = binding.accident?.location
                val geocoder = Geocoder(binding.root.context, Locale.getDefault())
                addressName =
                    geocoder.getFromLocation(
                        location?.latitude ?: 5.623,
                        location?.longitude ?: -0.184,
                        1
                    )[0].getAddressLine(0)
            } catch (ex: Exception) {
                debugLog(ex.localizedMessage)
            }
        }

        return addressName
    }

}