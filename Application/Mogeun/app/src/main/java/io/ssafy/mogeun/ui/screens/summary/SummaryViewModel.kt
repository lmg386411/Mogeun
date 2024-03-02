package io.ssafy.mogeun.ui.screens.summary

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.SummaryRepository
import io.ssafy.mogeun.model.BodyInfo
import io.ssafy.mogeun.model.BodyInfoResponse
import io.ssafy.mogeun.model.MostPerformedExercise
import io.ssafy.mogeun.model.MostPerformedExerciseResponse
import io.ssafy.mogeun.model.MostSetExercise
import io.ssafy.mogeun.model.MostSetExerciseResponse
import io.ssafy.mogeun.model.MostWeightedExercise
import io.ssafy.mogeun.model.MostWeightedExerciseResponse
import io.ssafy.mogeun.model.PerformedMuscleInfo
import io.ssafy.mogeun.model.PerformedMuscleInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SummaryViewModel(
    private val summaryRepository: SummaryRepository,
    private val keyRepository: KeyRepository
): ViewModel() {
    var userKey by mutableStateOf<Int?>(null)

    private val _summaryBodyInfoSuccess = MutableStateFlow(false)
    val summaryBodyInfoSuccess: StateFlow<Boolean> = _summaryBodyInfoSuccess.asStateFlow()
    var summaryBodyInfo by mutableStateOf<BodyInfo?>(null)

    private val _summaryPerformedMuscleSuccess = MutableStateFlow(false)
    val summaryPerformedMuscleSuccess: StateFlow<Boolean> = _summaryPerformedMuscleSuccess.asStateFlow()
    var summaryPerformedMuscle = mutableStateListOf<List<PerformedMuscleInfo>?>()

    private val _summaryExerciseMostSuccess = MutableStateFlow(false)
    val summaryExerciseMostSuccess: StateFlow<Boolean> = _summaryExerciseMostSuccess.asStateFlow()
    var summaryExerciseMost = mutableStateListOf<MostPerformedExercise?>()

    private val _summaryExerciseWeightSuccess = MutableStateFlow(false)
    val summaryExerciseWeightSuccess: StateFlow<Boolean> = _summaryExerciseWeightSuccess.asStateFlow()
    var summaryExerciseWeight = mutableStateListOf<MostWeightedExercise?>()

    private val _summaryExerciseSetSuccess = MutableStateFlow(false)
    val summaryExerciseSetSuccess: StateFlow<Boolean> = _summaryExerciseSetSuccess.asStateFlow()
    var summaryExerciseSet = mutableStateListOf<MostSetExercise?>()

    private val _summaryLoading = MutableStateFlow(false)
    val summaryLoading: StateFlow<Boolean> = _summaryLoading.asStateFlow()

    private val _summarySuccess = MutableStateFlow(false)
    val summarySuccess: StateFlow<Boolean> = _summarySuccess.asStateFlow()

    var summaryIndex = mutableIntStateOf(0)
    var muscleIndex = mutableIntStateOf(0)

    private fun updateUserKey(update: Int?) {
        userKey= update
    }

    private fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            launch(Dispatchers.Main) {
                updateUserKey(userKey)
            }
        }
    }

    fun summaryBody() {
        getUserKey()

        if (userKey !== null) {
            lateinit var ret: BodyInfoResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryBodyInfo(userKey.toString())
                Log.d("summaryBodyInfo", "$ret")

                if (ret.status == "OK" || ret.status == "BAD_REQUEST") {
                    _summaryBodyInfoSuccess.value = true
                    summaryBodyInfo = ret.data
                }
            }
        }
    }

    fun summaryPerformedMuscle() {
        if (userKey !== null) {
            lateinit var ret1: PerformedMuscleInfoResponse
            lateinit var ret2: PerformedMuscleInfoResponse
            lateinit var ret3: PerformedMuscleInfoResponse
            viewModelScope.launch {
                ret1 = summaryRepository.summaryPerformedMuscle(userKey.toString(), "1")
                Log.d("summaryPerformedMuscleAll", "$ret1")

                if (ret1.status == "OK" || ret1.status == "BAD_REQUEST") {
                    summaryPerformedMuscle.add(0, ret1.data)
                }

                ret2 = summaryRepository.summaryPerformedMuscle(userKey.toString(), "2")
                Log.d("summaryPerformedMuscleYear", "$ret2")

                if (ret2.status == "OK" || ret2.status == "BAD_REQUEST") {
                    summaryPerformedMuscle.add(1, ret2.data)
                }

                ret3 = summaryRepository.summaryPerformedMuscle(userKey.toString(), "3")
                Log.d("summaryPerformedMuscleMonth", "$ret3")

                if (ret3.status == "OK" || ret3.status == "BAD_REQUEST") {
                    _summaryPerformedMuscleSuccess.value = true
                    summaryPerformedMuscle.add(2, ret3.data)
                }
            }
        }
    }

    fun summaryExerciseMost() {
        if (userKey !== null) {
            lateinit var ret1: MostPerformedExerciseResponse
            lateinit var ret2: MostPerformedExerciseResponse
            lateinit var ret3: MostPerformedExerciseResponse
            viewModelScope.launch {
                ret1 = summaryRepository.summaryExerciseMost(userKey.toString(), "1")
                Log.d("summaryExerciseMostAll", "$ret1")

                if (ret1.status == "OK" || ret1.status == "BAD_REQUEST") {
                    summaryExerciseMost.add(0, ret1.data)
                }

                ret2 = summaryRepository.summaryExerciseMost(userKey.toString(), "2")
                Log.d("summaryExerciseMostYear", "$ret2")

                if (ret2.status == "OK" || ret2.status == "BAD_REQUEST") {
                    summaryExerciseMost.add(1, ret2.data)
                }

                ret3 = summaryRepository.summaryExerciseMost(userKey.toString(), "3")
                Log.d("summaryExerciseMostMonth", "$ret3")

                if (ret3.status == "OK" || ret3.status == "BAD_REQUEST") {
                    summaryExerciseMost.add(2, ret3.data)
                }
                _summaryExerciseMostSuccess.value = true
            }
        }
    }

    fun summaryExerciseWeight() {
        if (userKey !== null) {
            lateinit var ret1: MostWeightedExerciseResponse
            lateinit var ret2: MostWeightedExerciseResponse
            lateinit var ret3: MostWeightedExerciseResponse
            viewModelScope.launch {
                ret1 = summaryRepository.summaryExerciseWeight(userKey.toString(), "1")
                Log.d("summaryExerciseWeightAll", "$ret1")

                if (ret1.status == "OK" || ret1.status == "BAD_REQUEST") {
                    summaryExerciseWeight.add(0, ret1.data)
                }

                ret2 = summaryRepository.summaryExerciseWeight(userKey.toString(), "2")
                Log.d("summaryExerciseWeightYear", "$ret2")

                if (ret2.status == "OK" || ret2.status == "BAD_REQUEST") {
                    summaryExerciseWeight.add(1, ret2.data)
                }

                ret3 = summaryRepository.summaryExerciseWeight(userKey.toString(), "3")
                Log.d("summaryExerciseWeightMonth", "$ret3")

                if (ret3.status == "OK" || ret3.status == "BAD_REQUEST") {
                    _summaryExerciseWeightSuccess.value = true
                    summaryExerciseWeight.add(2, ret3.data)
                }
            }
        }
    }

    fun summaryExerciseSet() {
        if (userKey !== null) {
            lateinit var ret1: MostSetExerciseResponse
            lateinit var ret2: MostSetExerciseResponse
            lateinit var ret3: MostSetExerciseResponse
            viewModelScope.launch {
                ret1 = summaryRepository.summaryExerciseSet(userKey.toString(), "1")
                Log.d("summaryExerciseSetAll", "$ret1")

                if (ret1.status == "OK" || ret1.status == "BAD_REQUEST") {
                    summaryExerciseSet.add(0, ret1.data)
                }

                ret2 = summaryRepository.summaryExerciseSet(userKey.toString(), "2")
                Log.d("summaryExerciseSetYear", "$ret2")

                if (ret2.status == "OK" || ret2.status == "BAD_REQUEST") {
                    summaryExerciseSet.add(1, ret2.data)
                }

                ret3 = summaryRepository.summaryExerciseSet(userKey.toString(), "3")
                Log.d("summaryExerciseSetMonth", "$ret3")

                if (ret3.status == "OK" || ret3.status == "BAD_REQUEST") {
                    _summaryExerciseSetSuccess.value = true
                    summaryExerciseSet.add(2, ret3.data)
                }
            }
        }
    }

    fun updateSummarySuccess() {
        _summarySuccess.value = true
    }
}