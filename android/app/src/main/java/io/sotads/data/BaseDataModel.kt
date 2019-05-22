package io.sotads.data

import android.os.Parcelable

/**
 * Base data model
 */
interface BaseDataModel : Parcelable {
    val key: String
}