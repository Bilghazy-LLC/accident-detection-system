package io.sotads.core.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.sotads.data.EmtDataModel


@Dao
interface ADSDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createEMT(emtDataModel: EmtDataModel)

    @Query("SELECT * FROM emt WHERE `key` = :key")
    fun getEMT(key: String): LiveData<EmtDataModel>

    @Delete
    fun deleteEMT(emtDataModel: EmtDataModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEMT(emtDataModel: EmtDataModel)
}