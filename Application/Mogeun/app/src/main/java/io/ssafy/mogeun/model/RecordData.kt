package io.ssafy.mogeun.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineReport(
    @SerialName(value = "routine_report_key")
    val key: Int,
    @SerialName(value = "routine_name")
    val routineName: String,
    @SerialName(value = "start_time")
    val startTime: String,
    @SerialName(value = "end_time")
    val endTime: String,
)

@Serializable
data class MonthlyRoutine(
    val date: String,
    @SerialName(value = "routine_count")
    val routineCount: Int,
    @SerialName(value = "routine_reports")
    val routineReports: List<RoutineReport>
)

@Serializable
data class MonthlyResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<MonthlyRoutine>
)

@Parcelize
@Serializable
data class SetResult(
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Float,
    @SerialName(value = "target_rep")
    val targetRep: Int,
    @SerialName(value = "success_rep")
    val successRep: Int,
    @SerialName(value = "muscle_activity")
    val muscleActivity: List<Float>?,
    @SerialName(value = "muscle_fatigue")
    val muscleFatigue: List<Float>?
) : Parcelable

@Parcelize
@Serializable
data class Exercise(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "image_path")
    val imagePath: String,
    val sets:Int,
    val parts: List<String>,
    @SerialName(value = "muscle_image_paths")
    val muscleImagePaths: List<String>,
    @SerialName(value = "set_results")
    val setResults: List<SetResult>
) : Parcelable

@Serializable
data class RoutineInfoData(
    val name: String,
    val date: String,
    val calorie: Float,
    @SerialName(value = "perform_time")
    val performTime: Int,
    @SerialName(value = "total_sets")
    val totalSets: Int,
    @SerialName(value = "is_attached")
    val isAttached: Char,
    val exercises: List<Exercise>
)

@Serializable
data class RoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: RoutineInfoData?
)
