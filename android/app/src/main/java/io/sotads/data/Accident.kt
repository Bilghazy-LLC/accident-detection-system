package io.sotads.data

import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Accident(
    override val key: String,
    val driver: String,
//    var location: GeoPoint,
    val timestamp: Date = Date(),
    var available: Boolean = true,
    var dispatched: Boolean = false
) : BaseDataModel {

    constructor() : this("", "")
}