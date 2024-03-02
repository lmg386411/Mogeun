package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MuscleMassLog(
    @SerialName(value = "changed_time")
    val changedTime: String,
    @SerialName(value = "muscle_mass")
    val muscleMass: Float
)

@Serializable
data class BodyFatLog(
    @SerialName(value = "changed_time")
    val changedTime: String,
    @SerialName(value = "body_fat")
    val bodyFat: Float
)

@Serializable
data class BodyInfo(
    val userKey: Int,
    @SerialName(value = "body_fat_change_log")
    val bodyFatChangeLog: List<BodyFatLog>,
    @SerialName(value = "muscle_mass_change_log")
    val muscleMassChangeLog: List<MuscleMassLog>
)

@Serializable
data class BodyInfoResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: BodyInfo?
)

@Serializable
data class PerformedMuscleInfo(
    @SerialName(value = "exec_name")
    val execName: String,
    val parts: List<String>
)

@Serializable
data class PerformedMuscleInfoResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<PerformedMuscleInfo>?
)

@Serializable
data class MostPerformedExercise(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "image_path")
    val imagePath: String,
    @SerialName(value = "performed_count")
    val performed: Int
)

@Serializable
data class MostPerformedExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: MostPerformedExercise?
)

@Serializable
data class MostWeightedExercise(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "image_path")
    val imagePath: String,
    val weight: Float
)

@Serializable
data class MostWeightedExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: MostWeightedExercise?
)

@Serializable
data class MostSetExercise(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "image_path")
    val imagePath: String,
    @SerialName(value = "set_count")
    val set: Int
)

@Serializable
data class MostSetExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: MostSetExercise?
)