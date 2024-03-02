package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.AddAllExerciseRequest
import io.ssafy.mogeun.model.AddAllExerciseResponse
import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.DeleteRoutineRequest
import io.ssafy.mogeun.model.DeleteRoutineResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.model.ListMyExerciseResponse
import io.ssafy.mogeun.model.MyExerciseResponse
import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.model.UpdateRoutineNameRequest
import io.ssafy.mogeun.model.UpdateRoutineNameResponse
import io.ssafy.mogeun.model.UpdateRoutineRequest
import io.ssafy.mogeun.model.UpdateRoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RoutineRepository{
    suspend fun addRoutine(userKey: Int?, routineMame: String): AddRoutineResponse
    suspend fun getRoutineList(user_key: String): GetRoutineListResponse
    suspend fun listAllExercise(): ListAllExerciseResponse
    suspend fun listMyExercise(routineKey: Int?): ListMyExerciseResponse
    suspend fun addAllExercise(routineKey: Int?, execKeys: List<Int>): AddAllExerciseResponse
    suspend fun myExercise(execKey: Int?): MyExerciseResponse
    suspend fun updateRoutine(routineKey: Int?, execKeys: List<Int>): UpdateRoutineResponse
    suspend fun updateRoutineName(routineKey: Int?, routineName: String?): UpdateRoutineNameResponse
    suspend fun deleteRoutine(routineKey: Int): DeleteRoutineResponse

}


class NetworkRoutineRepository(
    private val mogeunApiService: MogeunApiService
): RoutineRepository {
    override suspend fun addRoutine(userKey:Int?, routineName: String): AddRoutineResponse {
        return mogeunApiService.addRoutine(AddRoutineRequest(userKey, routineName))
    }
    override suspend fun listAllExercise(): ListAllExerciseResponse {
        return mogeunApiService.listAllExercise()
    }

    override suspend fun getRoutineList(key: String): GetRoutineListResponse {
        return mogeunApiService.getRoutineList(key)
    }

    override suspend fun listMyExercise(routineKey: Int?): ListMyExerciseResponse{
        return mogeunApiService.listMyExercise(routineKey)
    }

    override suspend fun addAllExercise(routineKey: Int?, execKeys: List<Int>): AddAllExerciseResponse{
        return mogeunApiService.addAllExercise(AddAllExerciseRequest(routineKey, execKeys))
    }

    override suspend fun myExercise(execKey: Int?): MyExerciseResponse {
        return mogeunApiService.myExercise(execKey)
    }
    override suspend fun updateRoutine(routineKey: Int?, execKeys: List<Int>): UpdateRoutineResponse {
        return mogeunApiService.updateRoutine(UpdateRoutineRequest(routineKey, execKeys))
    }
    override suspend fun updateRoutineName(routineKey: Int?, routineName: String?): UpdateRoutineNameResponse {
        return mogeunApiService.updateRoutineName(UpdateRoutineNameRequest(routineKey, routineName))
    }
    override suspend fun deleteRoutine(routineKey: Int): DeleteRoutineResponse {
        return mogeunApiService.deleteRoutine(DeleteRoutineRequest(routineKey))
    }
}

