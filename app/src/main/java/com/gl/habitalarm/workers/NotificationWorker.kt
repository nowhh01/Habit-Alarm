package com.gl.habitalarm.workers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.gl.habitalarm.data.Habit
import com.gl.habitalarm.services.NotificationService
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

private const val TAG = "NotificationWorker"

class NotificationWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "doWork(): called")

        var result: Result
        try {
            val data: Data = inputData
            val days: BooleanArray = data.getBooleanArray(DATA_KEY_DAYS)!!
            val today = LocalDate.now().dayOfWeek

            // check if it is today
            if (days[today.value % 7]) {
                val context: Context = applicationContext
                val intent: Intent = NotificationService.createIntent(
                        context,
                        data.getLong(DATA_KEY_ID, 0L),
                        data.getString(DATA_KEY_NAME)!!)

                context.startService(intent)

                Log.d(TAG, "doWork(): startService() called")
            }

            result = Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "doWork(): exception thrown: ${e.message}")

            result = Result.failure()
        }

        return result
    }

    companion object {
        private const val REQUEST_TAG_NAME = "habitAlarm"
        private const val DATA_KEY_ID = "habitId"
        private const val DATA_KEY_NAME = "habitName"
        private const val DATA_KEY_DAYS = "habitDays"

        fun enqueueWorkerWithHabit(context: Context, habit: Habit) {
            enqueueWorkerWithHabit(context, habit.id, habit.name, habit.days, habit.notifyingTime!!)
        }

        fun enqueueWorkerWithHabit(context: Context, habitId: Long, habit: Habit) {
            enqueueWorkerWithHabit(context, habitId, habit.name, habit.days, habit.notifyingTime!!)
        }

        fun enqueueWorkerWithHabit(
                context: Context,
                id: Long,
                name: String,
                days: BooleanArray,
                notifyingTime: LocalTime
        ) {
            Log.d(TAG, "enqueueWorkerWithHabit(): called with habit $id, $name, $notifyingTime")

            val notifyingTime: LocalTime = notifyingTime
            var time = LocalTime.now()
            time = if (time.isAfter(notifyingTime)) {
                time.minus(notifyingTime.toSecondOfDay().toLong(), ChronoUnit.SECONDS)
            } else {
                notifyingTime.minus(time.toSecondOfDay().toLong(), ChronoUnit.SECONDS)
            }

            time = LocalTime.of(time.hour, time.minute)
            val notificationRequest = PeriodicWorkRequest
                    .Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                    .addTag(REQUEST_TAG_NAME)
                    .setInitialDelay(time.toSecondOfDay().toLong(), TimeUnit.SECONDS)
                    .setInputData(Data
                            .Builder()
                            .putLong(DATA_KEY_ID, id)
                            .putString(DATA_KEY_NAME, name)
                            .putBooleanArray(DATA_KEY_DAYS, days)
                            .build()
                    )
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    REQUEST_TAG_NAME + id,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    notificationRequest)
        }

        fun cancelWorkerWithHabit(context: Context, habitId: Long) {
            Log.d(TAG, "cancelWorkerWithHabit(): called with habit $habitId")

            WorkManager.getInstance(context)
                    .cancelUniqueWork(REQUEST_TAG_NAME + habitId)
        }

        fun cancelAllWorkers(context: Context) {
            Log.d(TAG, "cancelAllWorkers(): called")

            WorkManager.getInstance(context).cancelAllWork()
        }
    }
}