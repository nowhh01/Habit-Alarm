package com.gl.habitalarm.data

import androidx.room.Embedded

data class HabitWithRepetition(
    @Embedded
    val habit: Habit,
    @Embedded
    val repetition: Repetition? = null
) {
    fun areTheSameItem(other: HabitWithRepetition): Boolean {
        if (habit.id != other.habit.id) {
            return false
        }

        val otherRepetition: Repetition? = other.repetition
        if (repetition == null && otherRepetition == null) {
            return true
        }

        return if (repetition != null && otherRepetition != null) {
            repetition.id == otherRepetition.id
        } else false
    }

    fun areTheSameContent(other: HabitWithRepetition): Boolean {
        val otherHabit: Habit = other.habit
        var bHabitEqual = false
        if (habit.name == otherHabit.name && habit.days.contentEquals(otherHabit.days)) {
            if (habit.notifyingTime == null && otherHabit.notifyingTime == null) {
                bHabitEqual = true
            }
            if (habit.notifyingTime != null && otherHabit.notifyingTime != null
                && habit.notifyingTime.equals(otherHabit.notifyingTime)
            ) {
                bHabitEqual = true
            }
        }

        val otherRepetition: Repetition? = other.repetition
        var bRepetitionEqual = false
        if (repetition == null && otherRepetition == null) {
            bRepetitionEqual = true
        }

        if (repetition != null && otherRepetition != null) {
            if (repetition.state == otherRepetition.state
                && repetition.date == otherRepetition.date) {
                bRepetitionEqual = true
            }
        }

        return bHabitEqual && bRepetitionEqual
    }
}