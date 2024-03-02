package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.RoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RecordRepository {
    suspend fun recordMonthly(userKey: String, date: String): MonthlyResponse
    suspend fun recordRoutine(userKey: String, reportKey: String): RoutineResponse
}

class NetworkRecordRepository(
    private val mogeunApiService: MogeunApiService
): RecordRepository {
    override suspend fun recordMonthly(userKey: String, date: String): MonthlyResponse {
        return mogeunApiService.recordMonthly(userKey, date)
    }

    override suspend fun recordRoutine(userKey: String, reportKey: String): RoutineResponse {
        return mogeunApiService.recordRoutine(userKey, reportKey)
    }
}