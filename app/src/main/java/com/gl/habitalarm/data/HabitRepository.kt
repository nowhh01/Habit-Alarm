package com.gl.habitalarm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(private val mHabitDao: HabitDao) {
    fun getHabitById(id: Long): Flow<Habit> {
        return mHabitDao.getHabitById(id)
    }

    fun getHabitByName(name: String): Flow<Habit?>? {
        return mHabitDao.getHabitByName(name)
    }

    fun getHabitWithRepetitionsById(id: Long): Flow<HabitWithRepetitions> {
        return mHabitDao.getHabitWithRepetitionsById(id)
    }

    fun getHabitsByDayOfWeek(day: DayOfWeek): Flow<List<Habit>> {
        val value = day.value % 7
        return mHabitDao.getHabitsByDay(value)
    }

    fun getHabitsWithRepetitionsByDayOfWeek(day: DayOfWeek): Flow<List<HabitWithRepetitions>> {
        val value = day.value % 7
        return mHabitDao.getHabitsWithRepetitionsByDay(value)
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
        }
    }

    suspend fun updateHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val updatedItemCount = mHabitDao.update(habit)
        }
    }

    suspend fun removeHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val removedItemCount = mHabitDao.delete(habit)
        }
    }
}