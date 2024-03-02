package io.ssafy.mogeun.data

import kotlinx.coroutines.flow.Flow

interface EmgRepository {
    suspend fun insertEmg(emg: Emg)
    suspend fun deleteEmgData()
    fun getEmgData(startTime: Long, endTime: Long): List<Emg>
    fun getEmgStream(): Flow<Emg?>
}

class OfflineEmgRepository(private val emgDao: EmgDao): EmgRepository {
    override suspend fun insertEmg(emg: Emg) = emgDao.insert(emg)
    override suspend fun deleteEmgData() = emgDao.deleteEmgData()
    override fun getEmgData(startTime: Long, endTime: Long) = emgDao.getEmgData(startTime, endTime)
    override fun getEmgStream(): Flow<Emg?> = emgDao.getEmgStream()
}