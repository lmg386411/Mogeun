package io.ssafy.mogeun.ui.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.Key
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.SignInResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInRepository: UserRepository,
    private val keyRepository: KeyRepository
) : ViewModel() {
    private val _keyInput = mutableStateOf<Key>(Key(0, 1))
    private val _signInSuccess = MutableStateFlow(false)
    val signInSuccess: StateFlow<Boolean> = _signInSuccess.asStateFlow()
    var id by mutableStateOf("")
    var pwd by mutableStateOf("")
    var errorSignIn by mutableStateOf(false)

    fun updateText1(value: String) {
        id = value
    }
    fun updateText2(value: String) {
        pwd = value
    }
    fun updateErrorSignIn(value: Boolean) {
        errorSignIn = value
    }
    fun signIn() {
        lateinit var ret: SignInResponse
        viewModelScope.launch {
            ret = signInRepository.signIn(id, pwd)
            if (ret.message == "SUCCESS") {
                _signInSuccess.value = true
                setUserKey(ret.data)
            } else {
                updateErrorSignIn(true)
            }
        }
    }
    suspend fun setUserKey(key: Int) {
        _keyInput.value = _keyInput.value.copy(userKey = key)
        keyRepository.insertKey(Key(1, key))
    }
}
