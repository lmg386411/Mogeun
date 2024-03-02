package io.ssafy.mogeun.data

import android.content.Context

interface AppContainer {
    val dataLayerRepository: DataLayerRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {
    override val dataLayerRepository: DataLayerRepository by lazy {
        AndroidDataLayerRepository(context)
    }

}