package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetRequest(
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_number")
    val setNumber: Int,
    @SerialName(value = "muscle_avg")
    val muscleAvg: Float,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int,
    @SerialName(value = "success_rep")
    val successRep: Int,
    @SerialName(value = "muscle_fatigue")
    val muscleFatigue: Float,
    @SerialName(value = "start_time")
    val startTime: String,
    @SerialName(value = "end_time")
    val endTime: String
)

@Serializable
data class SetResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: String?
)