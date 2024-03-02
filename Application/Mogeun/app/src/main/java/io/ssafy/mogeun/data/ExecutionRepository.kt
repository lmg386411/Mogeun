package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.CalorieReportRequest
import io.ssafy.mogeun.model.CalorieReportResponse
import io.ssafy.mogeun.model.ClearPlanResponse
import io.ssafy.mogeun.model.EndRoutineRequest
import io.ssafy.mogeun.model.RoutineExecutionResponse
import io.ssafy.mogeun.model.SetExecutionRequest
import io.ssafy.mogeun.model.SetExecutionResponse
import io.ssafy.mogeun.model.SetInfo
import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.model.SetPlanRequest
import io.ssafy.mogeun.model.SetPlanResponse
import io.ssafy.mogeun.model.StartRoutineRequest
import io.ssafy.mogeun.network.MogeunApiService

interface ExecutionRepository {

    suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse
    suspend fun startRoutine(userKey: Int, routineKey: Int, isAttached: String): RoutineExecutionResponse
    suspend fun endRoutine(userKey: Int, reportKey: Int): RoutineExecutionResponse
    suspend fun reportSet(report: SetExecutionRequest): SetExecutionResponse
    suspend fun clearPlan(planKey: Int): ClearPlanResponse
    suspend fun setPlan(planKey: Int, setInfos: List<SetInfo>): SetPlanResponse
    suspend fun reportCalorie(reportKey: Int, calorie: Double): CalorieReportResponse
}

class NetworkExecutionRepository(
    private val mogeunApiService: MogeunApiService
): ExecutionRepository {
    override suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse {
        return mogeunApiService.getSetOfRoutine(planKey)
    }

    override suspend fun startRoutine(
        userKey: Int,
        routineKey: Int,
        isAttached: String
    ): RoutineExecutionResponse {
        return mogeunApiService.startRoutine(StartRoutineRequest(userKey, routineKey, isAttached))
    }

    override suspend fun endRoutine(userKey: Int, reportKey: Int): RoutineExecutionResponse {
        return mogeunApiService.endRoutine(EndRoutineRequest(userKey, reportKey))
    }

    override suspend fun reportSet(report: SetExecutionRequest): SetExecutionResponse {
        return mogeunApiService.reportSet(setExecutionRequest = report)
    }

    override suspend fun clearPlan(planKey: Int): ClearPlanResponse {
        return mogeunApiService.deletePlan(planKey)
    }

    override suspend fun setPlan(planKey: Int, setInfos: List<SetInfo>): SetPlanResponse {
        return mogeunApiService.setPlan(SetPlanRequest(planKey, setInfos))
    }

    override suspend fun reportCalorie(reportKey: Int, calorie: Double): CalorieReportResponse {
        return mogeunApiService.reportCalorie(CalorieReportRequest(reportKey, calorie))
    }
}