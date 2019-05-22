package io.sotads.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var car_number: String,
    var emergency_contact: String,
    val email: String,
    val name: String
) : BaseDataModel {

    @Ignore
    constructor() : this("", "", "", "", "")
}