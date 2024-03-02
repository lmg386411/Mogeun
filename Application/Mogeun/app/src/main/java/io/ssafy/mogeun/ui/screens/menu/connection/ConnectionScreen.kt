package io.ssafy.mogeun.ui.screens.menu.connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer3
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.BleDevice
import io.ssafy.mogeun.ui.AppViewModelProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(
    viewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.state.collectAsState()
    val msgState by viewModel.connectionMessage.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val connectionSuccessText = stringResource(R.string.connection_connection_success)

    LaunchedEffect(key1 = msgState) {
        if(msgState.display) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(msgState.message)
            }
            viewModel.resetMessage()
        }
    }

    LaunchedEffect(key1 = state.connectedDevices[0]) {
        if(state.connectedDevices[0].isNotNull()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("1$connectionSuccessText")
            }
        }
    }
    LaunchedEffect(key1 = state.connectedDevices[1]) {
        if(state.connectedDevices[1].isNotNull()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("2$connectionSuccessText")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BluetoothDeviceList(
            scannedDevices = state.scannedDevices,
            connectedDevices = state.connectedDevices,
            connect = viewModel::connect,
            disconnect = viewModel::disConnect,
            startScan = viewModel::startScan,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceList(
    scannedDevices: List<BluetoothDevice>,
    connectedDevices: List<BleDevice?>,
    connect: (BluetoothDevice) -> Unit,
    disconnect: (BleDevice) -> Unit,
    startScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scanning by rememberSaveable{ mutableStateOf(0)}
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFF7F7F7))
                    .shadow(0.5.dp)
            ) {
                Text(
                    text = stringResource(R.string.connection_connected_devices),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        items(connectedDevices) { device ->
            if(device.isNotNull()) {
                val isLeft = connectedDevices.indexOf(device) == 0
                ConnectedDeviceItem(device = device!!, onClick = disconnect, isLeft)
            }
        }
        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFF7F7F7))
                    .shadow(0.5.dp)
            ) {
                Text(
                    text = stringResource(R.string.connection_scanned_devices),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                IconButton(onClick = {
                    if(scanning == 0) {
                        startScan()
                        scanning = 3
                        coroutineScope.launch {
                            delay(1000)
                            scanning = 2
                            delay(1000)
                            scanning = 1
                            delay(1000)
                            scanning = 0
                        }
                    }
                }) {
                    if(scanning == 0) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "search devices")
                    }else {
                        Text("${scanning}s")
                    }
                }
            }
        }
        items(scannedDevices.filter { device -> !connectedDevices.contains(BleDevice(device.name?:"null", device.address)) }.filter {device -> device.name.isNotNull()}.filter { device -> device.name.contains("Movice") }) { device ->
            ScannedDeviceItem(device = device, onClick = connect)
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ScannedDeviceItem(device: BluetoothDevice, onClick: (BluetoothDevice) -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(device) }
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = device.name ?: "(이름없는 장치)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    text = device.address,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            Icon(
                imageVector = Icons.Default.BluetoothConnected,
                contentDescription = "connect ble",
                tint = Color(0xFF0000AA)
            )
            Text(text = stringResource(R.string.connection_connect))
        }
    }
}

@Composable
fun ConnectedDeviceItem(device: BleDevice, onClick: (BleDevice) -> Unit, isLeft: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(device) }
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .aspectRatio(1f)
                    .shadow(1.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = if(isLeft) stringResource(R.string.connection_left) else stringResource(R.string.connection_right))
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = device.name ?: "(이름없는 장치)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Text(
                    text = device.address,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            Icon(
                imageVector = Icons.Default.BluetoothDisabled,
                contentDescription = "disconnect ble",
                tint = Color(0xFF0000AA)
            )
            Text(text = stringResource(R.string.connection_disconnect))
        }
    }
}
