package com.gl.habitalarm.data

import android.content.Context
import com.gl.habitalarm.enums.EDay
import com.gl.habitalarm.workers.NotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    @ApplicationContext
    private val mContext: Context,
    private val mHabitDao: HabitDao
) {
    fun getHabitById(id: Long): Flow<Habit> {
        return mHabitDao.getHabitById(id)
    }

    fun getHabitByName(name: String): Flow<Habit> {
        return mHabitDao.getHabitByName(name)
    }

    fun getHabitWithRepetitionsById(id: Long): Flow<HabitWithRepetitions> {
        return mHabitDao.getHabitWithRepetitionsById(id)
    }

    fun getHabitsByDay(day: EDay): Flow<List<Habit>> {
        return mHabitDao.getHabitsByDay(day.ordinal)
    }

    fun getHabitsWithRepetitionsByDay(day: EDay): Flow<List<HabitWithRepetitions>> {
        return mHabitDao.getHabitsWithRepetitionsByDay(day.ordinal)
    }

    fun getHabitsWithRepetitionsByDate(date: LocalDate): Flow<List<HabitWithRepetition>> {
        val day = date.dayOfWeek.value % 7
        val epochDay = date.toEpochDay()
        return mHabitDao.getHabitsWithRepetitionsByDayAndDate(day, epochDay)
    }

    fun getHabitsWithRepetitions(): Flow<List<HabitWithRepetitions>> {
        return mHabitDao.getHabitsWithRepetitions()
    }

    fun getHabits(): Flow<List<Habit>> {
        return mHabitDao.getHabits()
    }

    suspend fun addHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val habitId = mHabitDao.insert(habit)
            if(habitId != 0L) {
                habit.notifyingTime?.apply {
                    NotificationWorker.enqueueWorkerWithHabit(mContext, habitId, habit)
                }
            }
        }
    }

    suspend fun updateHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val updatedItemCount = mHabitDao.update(habit)
            if(updatedItemCount != 0) {
                habit.notifyingTime?.apply {
                    NotificationWorker.enqueueWorkerWithHabit(mContext, habit)
                }
            }
        }
    }

    suspend fun removeHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val removedItemCount = mHabitDao.delete(habit)
            if(removedItemCount != 0) {
                habit.notifyingTime?.apply {
                    NotificationWorker.cancelWorkerWithHabit(mContext, habit.id)
                }
            }
        }
    }
}