package io.ssafy.mogeun.ui.screens.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.KeyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val keyRepository: KeyRepository,
) : ViewModel() {
    var userKey by mutableStateOf<Int?>(null)
    var dynamicMode by mutableStateOf<Boolean>(false)
    var darkMode by mutableStateOf<Boolean>(false)
    var lightMode by mutableStateOf<Boolean>(false)

    var settingLoaded by mutableStateOf(false)

    fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val dynamic = keyRepository.getDynamicMode()
            dynamicMode = dynamic?.userKey == 1
            val dark = keyRepository.getDarkMode()
            darkMode = dark?.userKey == 1
            val light = keyRepository.getLightMode()
            lightMode = light?.userKey == 1
            settingLoaded = true

            val key = keyRepository.getKey()
            val ret = key?.userKey
            Log.d("getUserKey", "사용자 키: $ret")
            userKey = ret
        }
    }
}