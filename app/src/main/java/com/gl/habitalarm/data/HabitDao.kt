package com.gl.habitalarm.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE habit_id = :id LIMIT 1")
    fun getHabitById(id: Long): Flow<Habit>

    @Query("SELECT * FROM habit WHERE name = :name LIMIT 1")
    fun getHabitByName(name: String?): Flow<Habit>

    @Transaction
    @Query("SELECT * FROM habit WHERE habit_id = :id LIMIT 1")
    fun getHabitWithRepetitionsById(id: Long): Flow<HabitWithRepetitions>

    @Query("SELECT * FROM habit WHERE days & (1 << :day)")
    fun getHabitsByDay(day: Int): LiveData<List<Habit>>

    @Transaction
    @Query("SELECT * FROM habit WHERE days & (1 << :day)")
    fun getHabitsWithRepetitionsByDay(day: Int): Flow<List<HabitWithRepetitions>>

    @Query("SELECT * FROM habit")
    fun getHabits(): LiveData<List<Habit>>

    @Transaction
    @Query("SELECT * FROM habit")
    fun getHabitsWithRepetitions(): LiveData<List<HabitWithRepetitions>>

    @Transaction
    @Query("SELECT * FROM habit " +
                "LEFT JOIN repetition ON repetition_habit_id = habit_id AND date = :date " +
                "WHERE days & (1 << :day)")
    fun getHabitsWithRepetitionsByDayAndDate(day: Int, date: Long)
            : LiveData<List<HabitWithRepetition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit): Int

    @Delete
    suspend fun delete(habit: Habit): Int
}