package io.ssafy.mogeun.data

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import io.ssafy.mogeun.MainActivity

class DataLayerListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == MOGEUN_SERVICE_START_PATH) {
            val startIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("VOICE_DATA", messageEvent.data)
            }
            startActivity(startIntent)
        }
    }

    companion object {
        private const val MOGEUN_SERVICE_START_PATH = "/mogeun_start"
    }
}