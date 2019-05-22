package io.sotads.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import java.util.*

//@Parcelize
data class Accident(
    /*override */val key: String,
                 val driver: DocumentReference?,
                 var location: GeoPoint,
                 val timestamp: Date = Date(),
                 var available: Boolean = true,
                 var dispatched: Boolean = false
) /*: BaseDataModel*/ {

    constructor() : this("", null, GeoPoint(5.654, -0.184))
}