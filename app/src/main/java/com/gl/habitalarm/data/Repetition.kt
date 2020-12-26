package com.gl.habitalarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gl.habitalarm.enums.ERepetitionState
import java.time.LocalDate

@Entity
data class Repetition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "repetition_id")
    val id: Long = 0,
    @ColumnInfo(name = "repetition_habit_id")
    val habitId: Long,
    val date: LocalDate = LocalDate.now(),
    val state: ERepetitionState
)