package io.sotads.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * [EmtDataModel] base class
 */
@Parcelize
@Entity(tableName = "emt")
data class EmtDataModel(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var name: String? = null,
    var available: Boolean = true,
    @SerializedName("emergency_type")
    var emergencyType: String = EmergencyType.MINOR
) : BaseDataModel {

    @Ignore
    constructor() : this("")

    object EmergencyType {
        const val MINOR = "Minor collision"
        const val MAJOR = "Motor Accident"
    }

}