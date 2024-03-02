package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import io.ssafy.mogeun.network.MogeunApiService

interface SetRepository {
    suspend fun getSet(setRequest: SetRequest): SetResponse
}

class NetworkSetRepository(
    private val mogeunApiService: MogeunApiService
): SetRepository {
    override suspend fun getSet(setRequest: SetRequest): SetResponse {
        return mogeunApiService.getSet(setRequest)
    }
}