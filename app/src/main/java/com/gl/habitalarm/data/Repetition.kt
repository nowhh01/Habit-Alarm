package com.gl.habitalarm.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Repetition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "repetition_id")
    val id: Long,
    @NonNull
    @ColumnInfo(name = "repetition_habit_id")
    val habitId: Long,
    @NonNull
    val data: LocalDate,
    val state: Int
) {
}