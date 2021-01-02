package com.gl.habitalarm

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "MainApplication"

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration(): Configuration {
        Log.d(TAG, "getWorkManagerConfiguration(): called")
        return Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .build()
    }
}