package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.AddAllExerciseRequest
import io.ssafy.mogeun.model.AddAllExerciseResponse
import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.BodyInfoResponse
import io.ssafy.mogeun.model.CalorieReportRequest
import io.ssafy.mogeun.model.CalorieReportResponse
import io.ssafy.mogeun.model.ClearPlanResponse
import io.ssafy.mogeun.model.DeleteRoutineRequest
import io.ssafy.mogeun.model.DeleteRoutineResponse
import io.ssafy.mogeun.model.DeleteUserRequest
import io.ssafy.mogeun.model.DeleteUserResponse
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.EndRoutineRequest
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.ListMyExerciseResponse
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.MostPerformedExerciseResponse
import io.ssafy.mogeun.model.MostSetExerciseResponse
import io.ssafy.mogeun.model.MostWeightedExerciseResponse
import io.ssafy.mogeun.model.MyExerciseResponse
import io.ssafy.mogeun.model.PerformedMuscleInfoResponse
import io.ssafy.mogeun.model.RoutineExecutionResponse
import io.ssafy.mogeun.model.RoutineResponse
import io.ssafy.mogeun.model.SetExecutionRequest
import io.ssafy.mogeun.model.SetExecutionResponse
import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.model.SetPlanRequest
import io.ssafy.mogeun.model.SetPlanResponse
import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.model.SignUpRequest
import io.ssafy.mogeun.model.SignUpResponse
import io.ssafy.mogeun.model.StartRoutineRequest
import io.ssafy.mogeun.model.UpdateRoutineNameRequest
import io.ssafy.mogeun.model.UpdateRoutineNameResponse
import io.ssafy.mogeun.model.UpdateRoutineRequest
import io.ssafy.mogeun.model.UpdateRoutineResponse
import io.ssafy.mogeun.model.UpdateUserRequest
import io.ssafy.mogeun.model.UpdateUserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("User/isJoined")
    suspend fun dupEmail(@Query("email") email: String): DupEmailResponse

    @POST("User/Enroll")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @GET("User/Detail")
    suspend fun getInbody(@Query("user_key") userKey: String): GetInbodyResponse

    @PUT("User/Log/Change/All")
    suspend fun updateUser(@Body updateUserRequest: UpdateUserRequest): UpdateUserResponse

    @GET("Routine/ListAll")
    suspend fun getRoutineList(@Query("user_key") userKey: String): GetRoutineListResponse

    @POST("Routine/Create")
    suspend fun addRoutine(@Body addRoutineRequest: AddRoutineRequest): AddRoutineResponse


    @GET("Result/Monthly")
    suspend fun recordMonthly(@Query("user_key") userKey: String, @Query("date") date: String): MonthlyResponse

    @GET("Result/Routine")
    suspend fun recordRoutine(@Query("user_key") userKey:String, @Query("routine_report_key") reportKey: String): RoutineResponse

    @GET("Exercise/ListAll")
    suspend fun listAllExercise(): ListAllExerciseResponse

    @POST("User/Exit")
    suspend fun deleteUser(@Body deleteUserRequest: DeleteUserRequest): DeleteUserResponse

    @POST("Report/Routine/Set")
    suspend fun getSet(@Body setRequest: SetRequest): SetResponse

    @GET("Routine/Plan/ListAll")
    suspend fun listMyExercise(@Query("routine_key") routineKey: Int?): ListMyExerciseResponse

    @POST("Routine/Plan/AddAll")
    suspend fun addAllExercise(@Body addAllExerciseRequest: AddAllExerciseRequest): AddAllExerciseResponse

    @GET("Exercise/List")
    suspend fun myExercise(@Query("exec_key") execKey: Int?): MyExerciseResponse

    @PUT("Routine/Plan/Edit")
    suspend fun updateRoutine(@Body updateRoutineRequest: UpdateRoutineRequest): UpdateRoutineResponse

    @GET("Routine/Set/ListAll")
    suspend fun getSetOfRoutine(@Query("plan_key") planKey: Int): SetOfRoutineResponse
    @PUT("Routine/Rename")
    suspend fun updateRoutineName(@Body updateRoutineNameRequest: UpdateRoutineNameRequest): UpdateRoutineNameResponse
    @PUT("Routine/Delete")
    suspend fun deleteRoutine(@Body deleteRoutineRequest: DeleteRoutineRequest): DeleteRoutineResponse

    @GET("Summary/LastLogs")
    suspend fun summaryBodyInfo(@Query("user_key") userKey: String): BodyInfoResponse

    @GET("Summary/ExerciseMuscle")
    suspend fun summaryPerformedMuscle(@Query("user_key") userKey: String, @Query("search_type") searchType: String): PerformedMuscleInfoResponse

    @GET("Summary/ExerciseMost")
    suspend fun summaryExerciseMost(@Query("user_key") userKey: String, @Query("search_type") searchType: String): MostPerformedExerciseResponse

    @GET("Summary/ExerciseWeight")
    suspend fun summaryExerciseWeight(@Query("user_key") userKey: String, @Query("search_type") searchType: String): MostWeightedExerciseResponse

    @GET("Summary/ExerciseSet")
    suspend fun summaryExerciseSet(@Query("user_key") userKey: String, @Query("search_type") searchType: String): MostSetExerciseResponse

    @POST("Report/Routine/Start")
    suspend fun startRoutine(@Body startRoutineRequest: StartRoutineRequest): RoutineExecutionResponse

    @PUT("Report/Routine/End")
    suspend fun endRoutine(@Body endRoutineRequest: EndRoutineRequest): RoutineExecutionResponse

    @POST("Report/Routine/Set")
    suspend fun reportSet(@Body setExecutionRequest: SetExecutionRequest): SetExecutionResponse

    @DELETE("Routine/Set/DeleteAll")
    suspend fun deletePlan(@Query("plan_key") planKey: Int): ClearPlanResponse

    @POST("Routine/Set/AddAll")
    suspend fun setPlan(@Body setPlanRequest: SetPlanRequest): SetPlanResponse

    @POST("Result/Create")
    suspend fun reportCalorie(@Body calorieReportRequest: CalorieReportRequest): CalorieReportResponse
}