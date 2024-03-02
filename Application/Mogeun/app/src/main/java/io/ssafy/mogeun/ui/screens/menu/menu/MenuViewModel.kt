package io.ssafy.mogeun.ui.screens.menu.menu

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.DeleteUserResponse
import kotlinx.coroutines.launch

class MenuViewModel(
    private val keyRepository: KeyRepository,
    private val UserRepository: UserRepository
) : ViewModel()  {
    var username by mutableStateOf<String>("")
    var pw by mutableStateOf<String>("")
    var deleteUserSuccess by mutableStateOf(false)
    var errorDeleteUser by mutableStateOf(false)

    fun updateDeleteUserSuccess(value: Boolean) {
        deleteUserSuccess = value
    }
    fun updateId(value: String) {
        username = value
    }
    fun updatePw(value: String) {
        pw = value
    }
    fun updateErrorDeleteUser(value: Boolean) {
        errorDeleteUser = value
    }
    fun deleteUserKey() {
        viewModelScope.launch {
            keyRepository.deleteKeyData()
        }
    }
    fun deleteUser() {
        lateinit var ret: DeleteUserResponse
        viewModelScope.launch {
            ret = UserRepository.deleteUser(username, pw)       // response 데이터에 객체 들어올수 있음
            Log.d("deleteUser", "$ret")
            if (ret.message == "SUCCESS") {
                updateDeleteUserSuccess(true)
                deleteUserKey()
            } else {
                updateErrorDeleteUser(true)
            }
        }
    }
}