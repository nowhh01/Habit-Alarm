package com.gl.habitalarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "habit_id")
    val id: Long = 0,
    val name: String,
    val days: BooleanArray,
    val notifyingTime: LocalTime?
)