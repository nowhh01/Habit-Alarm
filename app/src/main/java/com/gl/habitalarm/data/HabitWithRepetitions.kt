package com.gl.habitalarm.data

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithRepetitions(
    @Embedded
    val habit: Habit,
    @Relation(parentColumn = "habit_id", entityColumn = "repetition_habit_id")
    val repetitions: List<Repetition> = emptyList()
)