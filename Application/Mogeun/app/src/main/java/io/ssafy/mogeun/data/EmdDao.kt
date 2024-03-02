package io.ssafy.mogeun.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmgDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(emg: Emg)

    @Query("SELECT * FROM emgValue WHERE time BETWEEN :startTime AND :endTime")
    fun getEmgData(startTime: Long, endTime: Long): List<Emg>

    @Query("SELECT * FROM emgValue ORDER BY time DESC LIMIT 1")
    fun getEmgStream(): Flow<Emg>

    @Query("DELETE FROM emgValue")
    suspend fun deleteEmgData()
}