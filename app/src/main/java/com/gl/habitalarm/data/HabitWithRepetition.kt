package com.gl.habitalarm.data

import androidx.room.Embedded

data class HabitWithRepetition(
    @Embedded
    var habit: Habit?,
    @Embedded
    var repetition: Repetition?
)