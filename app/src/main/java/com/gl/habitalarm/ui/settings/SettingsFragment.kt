package com.gl.habitalarm.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.gl.habitalarm.R
import com.gl.habitalarm.services.NotificationService

private const val TAG = "SettingsFragment"

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d(TAG, "onCreatePreferences(): called with rootKey $rootKey")

        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val switchPref: SwitchPreferenceCompat? =
                findPreference(getString(R.string.dark_theme_key))
        switchPref?.apply {
            onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any ->
                        val bDarkTheme = newValue as Boolean
                        if (bDarkTheme) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }

                        true
                    }
        }

        val notificationPref: Preference? = findPreference(getString(R.string.notification_key))
        notificationPref?.apply {
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                NotificationService.createNotificationChannel(context)

                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotificationService.CHANNEL_ID)
                startActivity(intent)

                true
            }
        }
    }
}