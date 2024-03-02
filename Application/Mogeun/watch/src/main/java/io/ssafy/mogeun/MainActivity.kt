/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package io.ssafy.mogeun

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.Wearable
import io.ssafy.mogeun.ui.theme.MogeunTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val vibrator: Vibrator by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(Vibrator::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp(mainViewModel.execName.value, mainViewModel.timerString.value, mainViewModel.messageString.value, mainViewModel::startSet, mainViewModel::stopSet, mainViewModel::clearMessage, mainViewModel.progress.value)
        }

        val setObserver = Observer<Boolean> { setEnded ->
            if(setEnded) {
                vibrate()
                mainViewModel.resetSetEnded()
            }
        }
        val messageObserver = Observer<Boolean> { setEnded ->
            if(setEnded) {
                vibrate(500)
                mainViewModel.resetMessageReceived()
            }
        }

        mainViewModel.setEnded.observe(this, setObserver)
        mainViewModel.messageReceived.observe(this, messageObserver)
    }

    override fun onResume() {
        super.onResume()
        messageClient.addListener(mainViewModel)
    }

    override fun onPause() {
        super.onPause()
        messageClient.removeListener(mainViewModel)
    }

    private fun vibrate(time: Long = 1000) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(time)
        }
    }
}