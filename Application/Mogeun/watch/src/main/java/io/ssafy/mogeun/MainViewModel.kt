package io.ssafy.mogeun

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import io.ssafy.mogeun.data.DataLayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(
    private val dataLayerRepository: DataLayerRepository,
):
    ViewModel(),
    MessageClient.OnMessageReceivedListener {


    var execName = mutableStateOf<String?>(null)
    var timerString = mutableStateOf<String?>(null)
    var setEnded: MutableLiveData<Boolean> = MutableLiveData(false)
    var messageReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    var routineEnded = mutableStateOf(false)
    var messageString = mutableStateOf<String?>(null)
    val progress = mutableStateOf(0.1f)

    fun startSet() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.startSet()
        }
    }

    fun stopSet() {
        viewModelScope.launch(Dispatchers.IO) {
            dataLayerRepository.endSet()
        }
    }

    fun resetSetEnded() {
        setEnded.value = false
    }

    fun resetMessageReceived() {
        messageReceived.value = false
    }

    fun clearMessage() {
        messageString.value = null
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == MOGEUN_EXERCISE_NAME_MESSAGE_PATH) {
            execName.value = messageEvent.data.decodeToString()
        }
        else if (messageEvent.path == MOGEUN_ROUTINE_TIMER_MESSAGE_PATH) {
            timerString.value = messageEvent.data.decodeToString()
        }
        else if (messageEvent.path == MOGEUN_SET_ENDED_PATH) {
            setEnded.value = true
        }
        else if (messageEvent.path == MOGEUN_ROUTINE_STARTED_PATH) {
            routineEnded.value = false
        }
        else if (messageEvent.path == MOGEUN_ROUTINE_ENDED_PATH) {
            routineEnded.value = true
            execName.value = null
            timerString.value = null
            progress.value = 0.1f
        }
        else if (messageEvent.path == MOGEUN_SEND_MESSAGE_PATH) {
            messageString.value = messageEvent.data.decodeToString()
            messageReceived.value = true
        }
        else if (messageEvent.path == MOGEUN_SEND_Progress_PATH) {
            val msg = messageEvent.data.decodeToString()
            progress.value = msg.toFloat()
        }
    }

    companion object {
        private const val TAG = "datalayer"

        private const val WEAR_CAPABILITY = "mogeun_transcription"
        private const val MOGEUN_SERVICE_START_PATH = "/mogeun_start"
        private const val MOGEUN_EXERCISE_NAME_MESSAGE_PATH = "/mogeun_routine_name"
        private const val MOGEUN_ROUTINE_TIMER_MESSAGE_PATH = "/mogeun_routine_timer"
        private const val MOGEUN_SET_ENDED_PATH = "/mogeun_set_ended"
        private const val MOGEUN_ROUTINE_STARTED_PATH = "/mogeun_routine_started"
        private const val MOGEUN_ROUTINE_ENDED_PATH = "/mogeun_routine_ended"
        private const val MOGEUN_SEND_MESSAGE_PATH = "/mogeun_send_message"
        private const val MOGEUN_SEND_Progress_PATH = "/mogeun_send_progress"
        private const val MOGEUN_ROUTINE_START_SET_PATH = "/mogeun_start_set"
        private const val MOGEUN_ROUTINE_END_SET_PATH = "/mogeun_end_set"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val dataLayerRepository = (this[APPLICATION_KEY] as MogeunApplication).container.dataLayerRepository
                MainViewModel(dataLayerRepository = dataLayerRepository)
            }
        }
    }
}