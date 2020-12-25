package com.gl.habitalarm.di

import android.content.Context
import com.gl.habitalarm.data.HabitAlarmDatabase
import com.gl.habitalarm.data.HabitDao
import com.gl.habitalarm.data.RepetitionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): HabitAlarmDatabase {
        return HabitAlarmDatabase.getInstance(context)
    }

    @Provides
    fun provideHabitDao(database: HabitAlarmDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    fun provideRepetitionDao(database: HabitAlarmDatabase): RepetitionDao {
        return database.repetitionDao()
    }
}