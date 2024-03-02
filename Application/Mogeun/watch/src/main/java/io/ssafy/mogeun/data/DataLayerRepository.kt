package io.ssafy.mogeun.data

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import io.ssafy.mogeun.MogeunApplication

interface DataLayerRepository{
    suspend fun startSet()
    suspend fun endSet()
}

class AndroidDataLayerRepository(
    private val context: Context
): DataLayerRepository {
    private val messageClient by lazy { Wearable.getMessageClient(context) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(context) }

    override suspend fun startSet() {
        try {
            val nodes = Tasks.await(
                capabilityClient
                    .getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
            ).nodes

            nodes.map { node ->
                messageClient.sendMessage(
                    node.id,
                    MOGEUN_ROUTINE_START_SET_PATH,
                    byteArrayOf()
                )
            }

        } catch(exception: Exception) {
            Log.d(TAG, "start set failed: $exception")
        }
    }

    override suspend fun endSet() {
        try {
            val nodes = Tasks.await(
                capabilityClient
                    .getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
            ).nodes

            nodes.map { node ->
                messageClient.sendMessage(
                    node.id,
                    MOGEUN_ROUTINE_END_SET_PATH,
                    byteArrayOf()
                )
            }

        } catch(exception: Exception) {
            Log.d(TAG, "end set failed: $exception")
        }
    }

    companion object {
        private const val TAG = "datalayer"

        private const val WEAR_CAPABILITY = "mogeun_transcription"
        private const val MOGEUN_SERVICE_START_PATH = "/mogeun_start"
        private const val MOGEUN_EXERCISE_NAME_MESSAGE_PATH = "/mogeun_routine_name"
        private const val MOGEUN_ROUTINE_TIMER_MESSAGE_PATH = "/mogeun_routine_timer"
        private const val MOGEUN_ROUTINE_START_SET_PATH = "/mogeun_start_set"
        private const val MOGEUN_ROUTINE_END_SET_PATH = "/mogeun_end_set"
    }
}