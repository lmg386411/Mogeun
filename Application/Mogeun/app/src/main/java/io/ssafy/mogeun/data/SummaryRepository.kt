package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.BodyInfoResponse
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.MostPerformedExerciseResponse
import io.ssafy.mogeun.model.MostSetExerciseResponse
import io.ssafy.mogeun.model.MostWeightedExerciseResponse
import io.ssafy.mogeun.model.PerformedMuscleInfoResponse
import io.ssafy.mogeun.model.RoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface SummaryRepository {
    suspend fun summaryBodyInfo(userKey: String): BodyInfoResponse
    suspend fun summaryPerformedMuscle(userKey: String, searchType: String): PerformedMuscleInfoResponse
    suspend fun summaryExerciseMost(userKey: String, searchType: String): MostPerformedExerciseResponse
    suspend fun summaryExerciseWeight(userKey: String, searchType: String): MostWeightedExerciseResponse
    suspend fun summaryExerciseSet(userKey: String, searchType: String): MostSetExerciseResponse
}

class NetworkSummaryRepository(
    private val mogeunApiService: MogeunApiService
): SummaryRepository {
    override suspend fun summaryBodyInfo(userKey: String): BodyInfoResponse {
        return mogeunApiService.summaryBodyInfo(userKey)
    }

    override suspend fun summaryPerformedMuscle(userKey: String, searchType: String): PerformedMuscleInfoResponse {
        return mogeunApiService.summaryPerformedMuscle(userKey, searchType)
    }

    override suspend fun summaryExerciseMost(userKey: String, searchType: String): MostPerformedExerciseResponse {
        return mogeunApiService.summaryExerciseMost(userKey, searchType)
    }

    override suspend fun summaryExerciseWeight(userKey: String, searchType: String): MostWeightedExerciseResponse {
        return mogeunApiService.summaryExerciseWeight(userKey, searchType)
    }

    override suspend fun summaryExerciseSet(userKey: String, searchType: String): MostSetExerciseResponse {
        return mogeunApiService.summaryExerciseSet(userKey, searchType)
    }
}