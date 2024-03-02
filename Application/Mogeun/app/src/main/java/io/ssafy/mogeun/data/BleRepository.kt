package io.ssafy.mogeun.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.data.BluetoothUtils.Companion.findResponseCharacteristic
import io.ssafy.mogeun.model.BleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.UUID
import kotlin.concurrent.schedule
import kotlin.coroutines.coroutineContext

interface BleRepository{
    val scanResults: StateFlow<List<BluetoothDevice>>

    fun startScan()

    fun stopScan()

    val connectedDevices: StateFlow<List<BleDevice?>>

    val connecting: StateFlow<List<Boolean>>

    val sensorVal: StateFlow<List<Int>>

    fun connect(device: BluetoothDevice)

    fun send(cmd: Int, idx: Int)

    fun disconnect(device: BleDevice)

    val virtualEnabled: StateFlow<Boolean>

    fun attachVirtual()

    fun detachVirtual()
}

const val TAG = "ble"

@SuppressLint("MissingPermission")
class AndroidBleRepository(
    private val context: Context
): BleRepository {
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val bluetoothLeScanner by lazy {
        bluetoothAdapter?.bluetoothLeScanner
    }

    private var _scanResults = MutableStateFlow<List<BluetoothDevice>> (emptyList())
    override val scanResults: StateFlow<List<BluetoothDevice>>
        get() = _scanResults.asStateFlow()

    private val bleScanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device: BluetoothDevice = result.device

            Log.d(TAG, "result = $result")

            if(!scanResults.value.contains(device)) {
                _scanResults.update { devices -> devices + device }
            }
        }
    }

    private val scanSettings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    override fun startScan() {
        Log.d(TAG,"기기 검색")
        bluetoothLeScanner?.startScan(null, scanSettings, bleScanCallback)
        Timer("SettingUp", false).schedule(3000) { stopScan() }
    }

    override fun stopScan() {
        Log.d(TAG,"검색 중지")
        bluetoothLeScanner?.stopScan(bleScanCallback)
    }

    private var mGatt: MutableList<BluetoothGatt?> = mutableListOf(null, null)

    private var deviceMac = mutableListOf<String>("", "")

    private val _connectedDevices = MutableStateFlow<List<BleDevice?>> (listOf(null, null))
    override val connectedDevices: StateFlow<List<BleDevice?>>
        get() = _connectedDevices.asStateFlow()

    private val _connecting = MutableStateFlow<List<Boolean>> (listOf(false, false))
    override val connecting: StateFlow<List<Boolean>>
        get() = _connecting.asStateFlow()

    private var _sensorVal = MutableStateFlow(listOf(0, 0))
    override val sensorVal: StateFlow<List<Int>>
        get() = _sensorVal.asStateFlow()

    val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            Log.d("test", "gatt = $gatt")
            val idx = deviceMac.indexOf(gatt?.device?.address)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG,"연결성공")
                _connectedDevices.update { it ->
                    it.mapIndexed { connectedIdx, origConnected ->
                        if(connectedIdx == idx) {
                            BleDevice(gatt!!.device.name, gatt!!.device.address)
                        } else {
                            origConnected
                        }
                    }
                }

                mGatt[idx]?.discoverServices()
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG,"연결해제")
                _connectedDevices.update { it ->
                    it.mapIndexed { connectedIdx, origConnected ->
                        if(connectedIdx == idx) {
                            null
                        } else {
                            origConnected
                        }
                    }
                }
                _sensorVal.update { it ->
                    it.mapIndexed { connectedIdx, origVal ->
                        if(connectedIdx == idx) {
                            0
                        } else {
                            origVal
                        }
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.d(TAG,"블루투스 셋팅완료")

                    val respCharacteristic = findResponseCharacteristic(gatt)
                    if( respCharacteristic == null ) {
                        Log.e(TAG, "블루투스 커맨드를 찾지 못하였습니다.")
                        return
                    }
                    gatt.setCharacteristicNotification(respCharacteristic, true)
                    val descriptor: BluetoothGattDescriptor = respCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                }
                else -> {
                    Log.e(TAG,"블루투스 셋팅실패")
                }
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            when(status){
                BluetoothGatt.GATT_SUCCESS -> {
                    Log.d(TAG,"데이터 보내기 성공")
                }
                else -> {
                    Log.d(TAG,"데이터 보내기 실패")
                }
            }
        }

        //안드로이드 13이상 호출
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

                val idx = deviceMac.indexOf(gatt.device.address)
                Log.d("ble", "${value}")
                _sensorVal.update { orig -> orig.mapIndexed { origIdx, origVal ->
                    if(origIdx == idx) {
                        value.toString().toInt()
                    } else {
                        origVal
                    }
                } }
            }
        }

        //안드로이드 12까지 호출
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {

                val idx = deviceMac.indexOf(gatt?.device?.address)
                _sensorVal.update { orig -> orig.mapIndexed { origIdx, origVal ->
                    if(origIdx == idx) {
                        characteristic?.getStringValue(0).toString().toInt()
                    } else {
                        origVal
                    }
                } }
            }
        }
    }

    override fun connect(device: BluetoothDevice){
        Log.d(TAG,"블루투스 연결")
        if(!connectedDevices.value[0].isNotNull()) {
            deviceMac[0] = device.address
            mGatt[0] = device.connectGatt(context, false, gattCallback)
        } else if(!connectedDevices.value[1].isNotNull()) {
            deviceMac[1] = device.address
            mGatt[1] = device.connectGatt(context, false, gattCallback)
        }
    }

    override fun send(cmd: Int, idx: Int) {
        if(mGatt[idx] == null) return
        val cmdCharacteristic = BluetoothUtils.findCommandCharacteristic(mGatt[idx]!!)
        if (cmdCharacteristic == null) {
            Log.e(TAG, "Unable to find cmd characteristic")
            disconnect(connectedDevices.value[idx]!!)
            return
        }

        val cmdByteArray: ByteArray = "$cmd".toByteArray()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val ret: Int = mGatt[idx]!!.writeCharacteristic(cmdCharacteristic, cmdByteArray, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
            // check the result
            if( ret != BluetoothStatusCodes.SUCCESS ) {
                Log.e(TAG, "Failed to write command")
            }
        } else {
            cmdCharacteristic.value = cmdByteArray
            val ret: Boolean = mGatt[idx]!!.writeCharacteristic(cmdCharacteristic)
            if(!ret) {
                Log.e(TAG, "Failed to write command")
            }
        }
    }

    override fun disconnect(device: BleDevice) {
        val idx = connectedDevices.value.indexOf(device)
        Log.d(TAG, "블루투스 연결 해제")

        _connectedDevices.update { it ->
            it.mapIndexed { connectedIdx, origConnected ->
                if (connectedIdx == idx) {
                    null
                } else {
                    origConnected
                }
            }
        }
        mGatt[idx]?.disconnect()
        mGatt[idx]?.close()
        mGatt[idx] = null
        deviceMac[idx] = ""
    }

    private var _virtualEnabled = MutableStateFlow(false)
    override val virtualEnabled: StateFlow<Boolean>
        get() = _virtualEnabled.asStateFlow()

    override fun attachVirtual() {
        for(i in 0..1) {
            if(connectedDevices.value[i].isNotNull()) {
                disconnect(connectedDevices.value[i]!!)
            }
        }

        _connectedDevices.update {
            listOf(BleDevice("virtual_left", "--:--:--:--:--:--"), BleDevice("virtual_right", "--:--:--:--:--:--"))
        }
        _virtualEnabled.update { true }

        CoroutineScope(Dispatchers.Default).launch {
            while(virtualEnabled.value) {
                var dummyValue: Int = 0
                for( i in 1..500) {
                    _sensorVal.update {
                        listOf(dummyValue + (1..10).random(), dummyValue + (1..10).random())
                    }
                    dummyValue++
                    delay(20)
                }
            }
            _sensorVal.update { listOf(0, 0) }
        }
    }

    override fun detachVirtual() {
        _connectedDevices.update {
            listOf(null, null)
        }
        _sensorVal.update {
            listOf(0, 0)
        }
        _virtualEnabled.update { false }
    }
}