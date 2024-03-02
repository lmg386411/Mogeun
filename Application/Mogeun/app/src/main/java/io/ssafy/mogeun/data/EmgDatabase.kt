package io.ssafy.mogeun.data

import android.content.ClipData.Item
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Emg::class], version = 1, exportSchema = false)
abstract class EmgDatabase: RoomDatabase() {
    abstract fun emgDao(): EmgDao

    companion object {
        @Volatile
        private var Instance: EmgDatabase? = null

        fun getDatabase(context: Context): EmgDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, EmgDatabase::class.java, "emg_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}