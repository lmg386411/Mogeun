package io.ssafy.mogeun.ui.screens.routine.execution

import android.bluetooth.BluetoothDevice
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.model.BleDevice
import io.ssafy.mogeun.model.ListMyExerciseResponse
import io.ssafy.mogeun.model.SetOfRoutineDetail

data class SensorState(
    val connectedDevices: List<BleDevice?> = listOf(null, null)
)

data class EmgUiState (
    val emg: List<Emg?> = listOf(null, null),
    val emgAvg: List<Double> = listOf(0.0, 0.0),
    val emgList: List<List<Int>> = listOf(listOf(), listOf()),
    val emgMax: List<Int> = listOf(0, 0)
)

data class MuscleSensorValue(
    val muscleAvg: Double? = null,
    val muscleFatigue: Double? = null,
)

data class SetProgress(
    val setNumber: Int,
    val targetWeight: Int,
    val targetRep: Int,
    val successRep: Int = 0,
    val sensorData: List<MuscleSensorValue> = listOf(MuscleSensorValue(), MuscleSensorValue()),
    val startTime: Long? = null
)

data class SetOfPlan(
    val planKey: Int,
    val valueChanged: Boolean,
    val setOfRoutineDetail: List<SetProgress>,
)

data class RoutineState(
    val planList: ListMyExerciseResponse? = null,
    val planDetails: List<SetOfPlan> = listOf(),
    val showBottomSheet: Boolean = false,
    val routineInProgress: Boolean = false,
    val reportKey: Int? = null,
    val setInProgress: Boolean = false,
    val hasValidSet: Boolean = false,
)

data class ElapsedTime(
    val startTime: Long,
    val minute: Int,
    val second: Int,
)

data class InbodyInfo(
    val hasData: Boolean = false,
    val height: Double? = null,
    val weight: Double? = null,
    val muscleMass: Double? = null,
    val offset: Double? = null
)