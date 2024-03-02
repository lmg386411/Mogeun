package io.ssafy.mogeun.data

import kotlinx.coroutines.flow.Flow

interface KeyRepository {
    suspend fun insertKey(key: Key)
    fun getKey(): Key?
    suspend fun deleteKeyData()
    fun getDynamicMode(): Key?
    fun getDarkMode(): Key?
    fun getLightMode(): Key?
}

class OfflineKeyRepository(private val keyDao: KeyDao): KeyRepository {
    override suspend fun insertKey(key: Key) = keyDao.insert(key)
    override fun getKey(): Key? = keyDao.getKey(1)
    override suspend fun deleteKeyData() = keyDao.deleteKeyData()
    override fun getDynamicMode(): Key? = keyDao.getKey(2)

    override fun getDarkMode(): Key? = keyDao.getKey(3)

    override fun getLightMode(): Key? = keyDao.getKey(4)
}