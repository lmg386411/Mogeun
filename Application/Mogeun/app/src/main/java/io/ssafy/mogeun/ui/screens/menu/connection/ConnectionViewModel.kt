package io.ssafy.mogeun.ui.screens.menu.connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.BleRepository
import io.ssafy.mogeun.model.BleDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ConnectionViewModel(
    private val bleRepository: BleRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConnectionState())
    val state: StateFlow<ConnectionState> = combine(
        bleRepository.scanResults,
        bleRepository.sensorVal,
        bleRepository.connectedDevices,
        _state
    ) { scanResult, sensorVal, connectedDevices, state ->
        state.copy(
            scannedDevices = scanResult,
            connectedDevices = connectedDevices,
            sensorVal = sensorVal,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _state.value)

    private val _connectionMessage = MutableStateFlow(ConnectionMessage())
    val connectionMessage: StateFlow<ConnectionMessage>
        get() = _connectionMessage.asStateFlow()

    fun startScan() {
        bleRepository.startScan()
    }

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        if(bleRepository.virtualEnabled.value) {
            _connectionMessage.update {
                it.copy(true, "메뉴 - 앱 설정에서 가상 센서연결을 해제해주세요")
            }
            return
        }
        if(!device.name.startsWith("Movice_")) {
            _connectionMessage.update {
                it.copy(true, "Movice 디바이스가 아닙니다.")
            }
            return
        }

        bleRepository.connect(device)
    }

    fun disConnect(device: BleDevice) {
        if(bleRepository.virtualEnabled.value) {
            _connectionMessage.update {
                it.copy(true, "메뉴 - 앱 설정에서 가상 센서연결을 해제해주세요")
            }
            return
        }
        bleRepository.disconnect(device)
    }

    fun resetMessage() {
        _connectionMessage.update { it.copy(false, "") }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}