package io.ssafy.mogeun.ui.screens.menu.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.BleRepository
import io.ssafy.mogeun.data.Key
import io.ssafy.mogeun.data.KeyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel(
    private val bleRepository: BleRepository,
    private val keyRepository: KeyRepository
): ViewModel() {
    private var _useDynamic = MutableStateFlow(false)
    val useDynamic = _useDynamic.asStateFlow()

    private var _useDarkMode = MutableStateFlow(false)
    val useDarkMode = _useDarkMode.asStateFlow()

    private var _useLightMode = MutableStateFlow(false)
    val useLightMode = _useLightMode.asStateFlow()

    private var _useVirtual = MutableStateFlow(false)
    val useVirtual = _useVirtual.asStateFlow()

    fun switchModes(setDynamic: Boolean, setDarkMode: Boolean, setLightMode: Boolean) {
        _useDynamic.value = setDynamic
        _useDarkMode.value = setDarkMode
        _useLightMode.value = setLightMode

        viewModelScope.launch(Dispatchers.IO) {
            setUserKey(2, if(setDynamic) 1 else 0)
            setUserKey(3, if(setDarkMode) 1 else 0)
            setUserKey(4, if(setLightMode) 1 else 0)
        }
    }

    suspend fun setUserKey(idx: Int, value: Int) {
        keyRepository.insertKey(Key(idx, value))
    }

    fun enableVirtualSensor() {
        _useVirtual.value = true
        bleRepository.attachVirtual()
    }

    fun disableVirtualSensor() {
        _useVirtual.value = false
        bleRepository.detachVirtual()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val dynamic = keyRepository.getDynamicMode()
            _useDynamic.value = dynamic?.userKey == 1
            val dark = keyRepository.getDarkMode()
            _useDarkMode.value = dark?.userKey == 1
            val light = keyRepository.getLightMode()
            _useLightMode.value = light?.userKey == 1
            _useVirtual.value = bleRepository.virtualEnabled.value
        }
    }
}