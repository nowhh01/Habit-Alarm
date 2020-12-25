package com.gl.habitalarm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepetitionDao {
    @Query("SELECT * FROM repetition WHERE repetition_habit_id = :habitId AND date = :date LIMIT 1")
    fun getRepetitionByHabitIdAndDate(habitId: Long, date: Long): Flow<Repetition>

    @Query("SELECT * FROM repetition WHERE repetition_habit_id = :habitId")
    fun getRepetitionsByHabitId(habitId: Long): Flow<List<Repetition>>

    @Insert
    suspend fun insert(repetition: Repetition): Long

    @Update
    suspend fun update(repetition: Repetition): Int

    @Delete
    suspend fun delete(repetition: Repetition): Int
}