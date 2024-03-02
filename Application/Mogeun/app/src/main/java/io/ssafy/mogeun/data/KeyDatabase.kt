package io.ssafy.mogeun.data

import android.content.ClipData.Item
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Key::class], version = 1, exportSchema = false)
abstract class KeyDatabase: RoomDatabase() {
    abstract fun keyDao(): KeyDao

    companion object {
        @Volatile
        private var Instance: KeyDatabase? = null

        fun getDatabase(context: Context): KeyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, KeyDatabase::class.java, "key_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}