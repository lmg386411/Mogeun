package io.ssafy.mogeun

import android.app.Application
import android.content.Context
import io.ssafy.mogeun.data.AppContainer
import io.ssafy.mogeun.data.DefaultAppContainer

class MogeunApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        mContext = this
    }

    companion object {
        private lateinit var mContext: Context

        fun getContext(): Context {
            return mContext
        }
    }
}