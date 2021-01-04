package com.gl.habitalarm

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
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

    override fun onCreate() {
        Log.d(TAG, "onCreate(): called")

        super.onCreate()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val bDarkTheme = sharedPref.getBoolean(getString(R.string.dark_theme_key), false)
        if (bDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}