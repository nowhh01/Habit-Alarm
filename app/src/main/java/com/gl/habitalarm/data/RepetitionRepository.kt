package com.gl.habitalarm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepetitionRepository @Inject constructor(private val mRepetitionDao: RepetitionDao) {
    fun getRepetitionsByHabitId(id: Long): Flow<List<Repetition>> {
        return mRepetitionDao.getRepetitionsByHabitId(id)
    }

    fun getRepetitionByHabitIdAndLocalDate(id: Long, date: LocalDate): Flow<Repetition> {
        val epochDay = date.toEpochDay()
        return mRepetitionDao.getRepetitionByHabitIdAndDate(id, epochDay)
    }

    suspend fun addRepetition(repetition: Repetition) {
        withContext(Dispatchers.IO) {
            val repetitionId = mRepetitionDao.insert(repetition)
        }
    }

    suspend fun updateRepetition(repetition: Repetition) {
        withContext(Dispatchers.IO) {
            val updatedItemCount = mRepetitionDao.update(repetition)
        }
    }

    suspend fun removeRepetition(repetition: Repetition) {
        withContext(Dispatchers.IO) {
            val removedItemCount = mRepetitionDao.delete(repetition)
        }
    }
}