package com.gl.habitalarm.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class DBTypeConverters {
    @TypeConverter
    fun localDateToLong(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun longToLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }

    @TypeConverter
    fun localTimeToLong(time: LocalTime?): Int? {
        return time?.toSecondOfDay()
    }

    @TypeConverter
    fun longToLocalTime(epochTime: Int?): LocalTime? {
        return if (epochTime != null) {
            LocalTime.ofSecondOfDay(epochTime.toLong())
        } else null
    }

    @TypeConverter
    fun booleanArrayToInt(booleans: BooleanArray): Int {
        var packedValue = 0
        var current = 1
        for (b in booleans) {
            if (b) {
                packedValue = packedValue or current
            }
            current = current shl 1
        }
        return packedValue
    }

    @TypeConverter
    fun intToBooleanArray(packedValue: Int): BooleanArray {
        val booleans = BooleanArray(7)
        var current = 1
        for (i in 0..6) {
            if (packedValue and current != 0) {
                booleans[i] = true
            }
            current = current shl 1
        }
        return booleans
    }
}