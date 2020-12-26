package com.gl.habitalarm.data

import androidx.room.Embedded

data class HabitWithRepetition(
    @Embedded
    val habit: Habit,
    @Embedded
    val repetition: Repetition? = null
)