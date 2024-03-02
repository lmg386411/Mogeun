package io.ssafy.mogeun.ui.screens.routine.searchRoutine

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.DeleteRoutineResponse
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.model.UpdateRoutineNameResponse
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoutineViewModel(
    private val UserRepository: UserRepository,
    private val keyRepository: KeyRepository,
    private val RoutineRepository: RoutineRepository
) : ViewModel() {
    var muscleMass by mutableStateOf<Double?>(null)
    var bodyFat by mutableStateOf<Double?>(null)
    var userKey by mutableStateOf<Int?>(null)
    var tmp by mutableStateOf<GetRoutineListResponse?>(null)
    var username by mutableStateOf<String?>(null)
    var newRoutineName = mutableStateOf<String> ("")

    fun updateMuscleMass(value: Double?) {
        muscleMass = value
    }
    fun updateBodyFat(value: Double?) {
        bodyFat = value
    }
    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun updateUsername(value: String?) {
        username = value
    }
    fun updateRoutineName(value: String) {
        newRoutineName.value = value
    }
    fun getInbody() {
        lateinit var ret: GetInbodyResponse
        viewModelScope.launch {
            ret = UserRepository.getInbody(userKey.toString())
            Log.d("getInbody", "$ret")
            updateMuscleMass(ret.data.muscleMass)
            updateBodyFat(ret.data.bodyFat)
            updateUsername(ret.data.userName)
            Log.d("updateUserKey", "${userKey}")
        }
    }
    fun getRoutineList() {
        lateinit var ret: GetRoutineListResponse
        viewModelScope.launch {
            ret = RoutineRepository.getRoutineList(userKey.toString())
            Log.d("RoutineList", "$ret")
            tmp = ret
        }
    }
    fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            launch(Dispatchers.Main) {
                updateUserKey(userKey)
            }
        }
    }
    fun updateRoutineName(index: Int, newName: String) {
        lateinit var ret: UpdateRoutineNameResponse
        viewModelScope.launch {
            tmp?.let { response ->
                val routineKey = response.data.getOrNull(index)?.routineKey
                if (routineKey != null) {
                    ret = RoutineRepository.updateRoutineName(routineKey, newName)
                    Log.d("updateRoutineName", "$ret")
                    getRoutineList()
                }
            }
        }
    }
    fun deleteRoutine(index: Int) {
        lateinit var ret: DeleteRoutineResponse
        viewModelScope.launch {
            tmp?.let { response ->
                val routineKey = response.data.getOrNull(index)?.routineKey
                if (routineKey != null) {
                    ret = RoutineRepository.deleteRoutine(routineKey)
                    Log.d("deleteRoutine", "$ret")
                    getRoutineList()
                }
            }
        }
    }
}