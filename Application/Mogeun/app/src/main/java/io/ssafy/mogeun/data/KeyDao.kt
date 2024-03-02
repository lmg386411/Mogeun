package io.ssafy.mogeun.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: Key)
    @Query("SELECT * FROM keyValue WHERE id = :id")
    fun getKey(id: Int): Key
    @Query("DELETE FROM keyValue")
    suspend fun deleteKeyData()
}