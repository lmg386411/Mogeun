package io.ssafy.mogeun.ui.screens.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.SignUpResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val signInRepository: UserRepository): ViewModel() {
    var dupEmailSuccess by mutableStateOf<Boolean>(false)
    var checkEmail by mutableIntStateOf(0)
    var id by mutableStateOf("")
    var password by mutableStateOf("")
    var checkingPassword by mutableStateOf("")
    var nickname by mutableStateOf("")
    var selectedGender by mutableStateOf("")
    var height by mutableStateOf<Double?>(null)
    var weight by mutableStateOf<Double?>(null)
    var muscleMass by mutableStateOf<Double?>(null)
    var bodyFat by mutableStateOf<Double?>(null)
    var inputForm by mutableIntStateOf(1)
    var firstText by mutableStateOf("")
    var successSignUp by mutableStateOf<Boolean>(false)
    var alertDupEmail by mutableStateOf(false)
    var alertPassword by mutableStateOf(false)
    var alertInput by mutableStateOf(false)

    fun updateDupEmailSuccess(value: Boolean) {
        dupEmailSuccess = value
    }
    fun updateSuccessSignUp(value: Boolean) {
        successSignUp = value
    }
    fun updateCheckEmail(value: Int) {
        checkEmail = value
    }
    fun updateId(value: String) {
        id = value
    }
    fun updatePassword(value: String) {
        password = value
    }
    fun updateCheckingPassword(value: String) {
        checkingPassword = value
    }
    fun updateNickname(value: String) {
        nickname = value
    }
    fun updateSelectedGender(value: String) {
        selectedGender = value
    }
    fun updateHeight(value: Double?) {
        height = value
    }
    fun updateWeight(value: Double?) {
        weight = value
    }
    fun updateMuscleMass(value: Double?) {
        muscleMass = value
    }
    fun updateBodyFat(value: Double?) {
        bodyFat = value
    }
    fun updateInputForm(value: Int) {
        inputForm = value
    }
    fun updateFirstText(value: String) {
        firstText = value
    }
    fun updateAlertDupEmail(value: Boolean) {
        alertDupEmail = value
    }
    fun updateAlertPassword(value: Boolean) {
        alertPassword = value
    }
    fun updateAlertInput(value: Boolean) {
        alertInput = value
    }
    fun dupEmail() {
        lateinit var ret: DupEmailResponse
        viewModelScope.launch {
            ret = signInRepository.dupEmail(id)
            if(ret.data.joinState == "가입하지 않은 회원") {
                updateDupEmailSuccess(true)
                updateCheckEmail(1)
            } else {
                updateCheckEmail(2)
            }
        }
    }
    fun signUp() {
        if (checkEmail == 1) {
            lateinit var ret: SignUpResponse
            viewModelScope.launch {
                ret = signInRepository.signUp(
                    id,
                    password,
                    nickname,
                    selectedGender,
                    height,
                    weight,
                    muscleMass,
                    bodyFat
                )
                updateSuccessSignUp(true)
            }
        } else {
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val signInRepository = application.container.userDataRepository
                SignupViewModel(signInRepository = signInRepository)
            }
        }
    }
}