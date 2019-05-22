package io.sotads.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    @SerializedName("car_number")
    var carNumber: String,
    @SerializedName("emergency_contact")
    var emergencyContact: String,
    val email: String,
    val name: String
) : BaseDataModel {

    @Ignore
    constructor() : this("", "", "", "", "")
}