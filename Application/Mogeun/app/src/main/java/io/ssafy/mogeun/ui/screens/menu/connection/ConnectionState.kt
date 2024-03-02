package io.ssafy.mogeun.ui.screens.menu.connection

import android.bluetooth.BluetoothDevice
import io.ssafy.mogeun.model.BleDevice

data class ConnectionState(
    val scannedDevices: List<BluetoothDevice> = listOf(),
    val connectedDevices: List<BleDevice?> = listOf(null, null),
    val sensorVal: List<Int> = listOf(0, 0),
)

data class ConnectionMessage(
    val display: Boolean = false,
    val message: String = ""
)