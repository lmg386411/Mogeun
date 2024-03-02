package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetOfRoutineDetail(
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int
)

@Serializable
data class SetOfRoutineResponseData(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "set_amount")
    val setAmount: Int,
    @SerialName(value = "set_details")
    val setDetails: List<SetOfRoutineDetail>
)

@Serializable
data class SetOfRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: SetOfRoutineResponseData?
)

@Serializable
data class StartRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "is_attached")
    val isAttached: String
)

@Serializable
data class EndRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "routine_report_key")
    val reportKey: Int,
)

@Serializable
data class SensorData(
    @SerialName(value = "sensor_number")
    val sensorNumber: Int,
    @SerialName(value = "muscle_average")
    val muscleAvg: Double,
    @SerialName(value = "muscle_fatigue")
    val muscleFatigue: Double
)

@Serializable
data class RoutineExecutionResponseData(
    @SerialName(value = "user_key")
    val userKey: Int,
    val email: String?,
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "report_key")
    val reportKey: Int?,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "is_attached")
    val isAttached: String,
    @SerialName(value = "start_time")
    val startTime: String?,
    @SerialName(value = "end_time")
    val endTime: String?,
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int?,
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int,
    @SerialName(value = "success_rep")
    val successRep: Int,
    @SerialName(value = "muscle_acts")
    val muscleActs: List<SensorData>?
)

@Serializable
data class RoutineExecutionResponse(
    val code: Int,
    val status: String,
    val message: String?,
    val data: RoutineExecutionResponseData?
)

@Serializable
data class SetExecutionRequest(
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int,
    @SerialName(value = "success_rep")
    val successRep: Int,
    @SerialName(value = "start_time")
    val startTime: String,
    @SerialName(value = "end_time")
    val endTime: String,
    @SerialName(value = "muscle_acts")
    val muscleActs: List<SensorData>
)

@Serializable
data class SetExecutionResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: RoutineExecutionResponseData?
)

@Serializable
data class ClearSetResponseData(
    @SerialName(value = "user_key")
    val userKey : Int,
    @SerialName(value = "user_email")
    val userEmail : String?,
    @SerialName(value = "routine_name")
    val routineName : String?,
    @SerialName(value = "routine_key")
    val routineKey : Int,
    @SerialName(value = "exec_key")
    val execKey : Int,
    @SerialName(value = "total_sets")
    val totalSets : Int,
    @SerialName(value = "plan_key")
    val planKey : Int,
    @SerialName(value = "set_key")
    val setKey : Int,

    )

@Serializable
data class ClearPlanResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: Int
)

@Serializable
data class SetInfo(
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int,
)

@Serializable
data class SetPlanRequest(
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_info")
    val setInfo: List<SetInfo>
)

@Serializable
data class SetPlanResponseData(
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_info")
    val setInfo: List<SetInfo>
)

@Serializable
data class SetPlanResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: SetPlanResponseData
)

@Serializable
data class CalorieReportRequest(
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int,
    @SerialName(value = "consume_calorie")
    val consumeCalorie: Double,
)

@Serializable
data class CalorieReportResponseData(
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int,
    @SerialName(value = "consume_calorie")
    val consumeCalorie: Double
)

@Serializable
data class CalorieReportResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: CalorieReportResponseData?
)