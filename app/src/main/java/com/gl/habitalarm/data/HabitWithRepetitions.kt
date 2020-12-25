package com.gl.habitalarm.data

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithRepetitions(
    @Embedded
    var habit: Habit,
    @Relation(parentColumn = "habit_id", entityColumn = "repetition_habit_id")
    var repetitions: List<Repetition> = emptyList()
)