package com.gl.habitalarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Habit::class, Repetition::class], version = 1, exportSchema = false)
@TypeConverters(DBTypeConverters::class)
abstract class HabitAlarmDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun repetitionDao(): RepetitionDao

    companion object {
        @Volatile
        private var mINSTANCE: HabitAlarmDatabase? = null

        fun getInstance(context: Context): HabitAlarmDatabase {
            return mINSTANCE ?: synchronized(this) {
                mINSTANCE ?: buildDatabase(context).also { mINSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): HabitAlarmDatabase{
            return Room.databaseBuilder(
                    context.applicationContext,
                    HabitAlarmDatabase::class.java,
                    "habit_alarm_db"
                )
                .allowMainThreadQueries()
                .build()
        }
    }
}