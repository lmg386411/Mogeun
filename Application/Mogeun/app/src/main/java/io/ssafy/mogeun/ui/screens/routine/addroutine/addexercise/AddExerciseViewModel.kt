package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.AddAllExerciseResponse
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.ListAllExerciseResponsedata
import io.ssafy.mogeun.model.ListMyExerciseResponse
import io.ssafy.mogeun.model.ListMyExerciseResponseData
import io.ssafy.mogeun.model.UpdateRoutineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddExerciseViewModel(
    private val keyRepository: KeyRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {
    private val _listAllExerciseSuccess = MutableStateFlow(false)
    var exerciseList = mutableStateListOf<ListAllExerciseResponsedata>()
    private val _addRoutineSuccess = MutableStateFlow(false)
    private val _addAllExerciseSuccess = MutableStateFlow(false)
    var userKey by mutableStateOf<Int?>(null)
    var routineKey by mutableStateOf<Int?>(null)
    var myExerciseList: List<ListMyExerciseResponseData> = mutableStateListOf()
    var successSearch by mutableStateOf<Boolean>(false)

    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun updateRoutineKey(value: Int?) {
        routineKey = value
    }
    fun updateSuccessSearch(value: Boolean) {
        successSearch = value
    }
    fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            val userKey = key?.userKey
            launch(Dispatchers.Main) {
                updateUserKey(userKey)
            }
        }
    }
    fun listAllExercise() {
        lateinit var ret: ListAllExerciseResponse
        viewModelScope.launch {
            ret = routineRepository.listAllExercise()
            if (ret.message == "SUCCESS") {
                _listAllExerciseSuccess.value = true
                val uniqueExercises = ret.data.distinctBy { it.name }
                exerciseList.clear()
                exerciseList.addAll(uniqueExercises)
            }
        }
    }
    fun addRoutine(userKey: Int?, routineName: String) {
        lateinit var ret: AddRoutineResponse
        viewModelScope.launch {
            ret = routineRepository.addRoutine(userKey, routineName)
            if (ret.message == "SUCCESS") {
                _addRoutineSuccess.value = true
                updateRoutineKey(ret.data.routineKey)
            }
        }
    }
    fun addAllExercise(routineKey: Int?, execKeys: List<Int>){
        lateinit var ret: AddAllExerciseResponse
        viewModelScope.launch{
            ret = routineRepository.addAllExercise(routineKey, execKeys)
            if(ret.message == "SUCCESS"){
                _addAllExerciseSuccess.value = true
            }
        }
    }
    fun updateRoutine(routineKey: Int?, execKeys: List<Int>){
        lateinit var ret: UpdateRoutineResponse
        viewModelScope.launch{
            ret = routineRepository.updateRoutine(routineKey, execKeys)
            if(ret.message == "SUCCESS"){
                _addAllExerciseSuccess.value = true
            }
        }
    }
    fun listMyExercise(routineKey: Int?){
        lateinit var ret: ListMyExerciseResponse
        viewModelScope.launch{
            ret = routineRepository.listMyExercise(routineKey)
            if (ret.message == "SUCCESS"){
                myExerciseList = ret.data
                updateSuccessSearch(true)
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val routineRepository = application.container.routineRepository
                val keyRepository = application.container.keyRepository
                AddExerciseViewModel(routineRepository = routineRepository, keyRepository = keyRepository)
            }
        }
    }
}