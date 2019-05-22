package io.sotads.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.sotads.core.util.ROOM_DB_NAME
import io.sotads.data.Driver
import io.sotads.data.EmtDataModel

@Database(entities = [EmtDataModel::class, Driver::class], version = 1, exportSchema = true)
abstract class ADSDatabase : RoomDatabase() {

    abstract fun dao(): ADSDao

    companion object {
        @Volatile
        private var instance: ADSDatabase? = null

        fun get(context: Context): ADSDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, ADSDatabase::class.java, ROOM_DB_NAME)
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }

}