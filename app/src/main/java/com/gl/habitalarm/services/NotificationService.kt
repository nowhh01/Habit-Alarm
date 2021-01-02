package com.gl.habitalarm.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gl.habitalarm.R
import com.gl.habitalarm.ui.home.HabitActivity

private const val TAG = "NotificationService"

class NotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind(): called")
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate(): called")

        super.onCreate()

        // Create NotificationChannel
        val name: CharSequence = getString(R.string.app_name)
        val description = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand(): called with flags $flags, startId $startId")

        val habitId = intent.getLongExtra(INTENT_KEY_ID, 0L)
        val habitName = intent.getStringExtra(INTENT_KEY_NAME)
        val newIntent = Intent(this, HabitActivity::class.java)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(this, 0, newIntent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(habitName)
                // Set the intent that will fire when the user taps the notification
                .setContentText(getString(R.string.notification_content))
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_NOTIFICATION)
                .setAutoCancel(true) // For Android 7.1 and lower
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(
                habitId.toInt(),
                builder.build())

        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private const val CHANNEL_ID = "HabitAlarm"
        private const val GROUP_KEY_NOTIFICATION = "com.gl.habitalarm.services.group_key"
        private const val INTENT_KEY_ID = "com.gl.habitalarm.services.id"
        private const val INTENT_KEY_NAME = "com.gl.habitalarm.services.name"
        private const val INTENT_KEY_DAYS = "com.gl.habitalarm.services.days"
        private const val INTENT_KEY_HOUR = "com.gl.habitalarm.services.hour"
        private const val INTENT_KEY_MINUTE = "com.gl.habitalarm.services.minute"

        fun createIntent(packageContext: Context?, id: Long, name: String): Intent {
            Log.d(TAG, "createIntent(): called with id $id, name $name")

            return Intent(packageContext, NotificationService::class.java)
                    .putExtra(INTENT_KEY_ID, id)
                    .putExtra(INTENT_KEY_NAME, name)
        }

        fun createIntent(
                packageContext: Context?,
                id: Long,
                name: String,
                days: BooleanArray,
                hour: Int,
                minute: Int
        ): Intent {
            Log.d(TAG, "createIntent(): called with id $id, name $name, hour $hour, minute $minute")

            return Intent(packageContext, NotificationService::class.java)
                    .putExtra(INTENT_KEY_ID, id)
                    .putExtra(INTENT_KEY_NAME, name)
                    .putExtra(INTENT_KEY_DAYS, days)
                    .putExtra(INTENT_KEY_HOUR, hour)
                    .putExtra(INTENT_KEY_MINUTE, minute)
        }
    }
}