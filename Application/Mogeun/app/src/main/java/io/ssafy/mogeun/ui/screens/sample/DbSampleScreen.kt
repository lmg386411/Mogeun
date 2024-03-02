package io.ssafy.mogeun.ui.screens.sample

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.theme.MogeunTheme
import kotlinx.coroutines.launch

@Composable
fun DbSampleScreen(viewModel: DbSampleViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val coroutineScope = rememberCoroutineScope()

    val emgLastData by viewModel.emgStream.collectAsState()
    val emgInput = viewModel.emgInput.value

    Column(modifier = Modifier
        .fillMaxSize()
    ) {

        Text("마지막 db emg 값 -------------")
        Text("디바이스 번호 : ${emgLastData?.deviceId ?: "데이터 없음"}")
        Text("부작 위치 : ${emgLastData?.sensingPart ?: "데이터 없음"}")
        Text("센서 값 : ${emgLastData?.value ?: "데이터 없음"}")

        Spacer(modifier = Modifier.height(40.dp))

        Text("로컬 emg 값 -------------")
        TextField(value = "${emgInput.deviceId}", onValueChange = {
            Log.d("check", "왜안댐? $it")
            viewModel.setDeviceId(it.toInt())
        })
        TextField(value = "${emgInput.sensingPart}", onValueChange = {
            viewModel.setSensingPart(it)
        })
        TextField(value = "${emgInput.value}", onValueChange = {
            viewModel.setValue(it.toInt())
        })

        Button(onClick = {
            coroutineScope.launch {
                viewModel.saveData()
            }
        }) {
            Text(text = "DB에 저장")
        }
    }
}


@Preview
@Composable
fun TestScreenPreview() {
    MogeunTheme {
        DbSampleScreen()
    }
}