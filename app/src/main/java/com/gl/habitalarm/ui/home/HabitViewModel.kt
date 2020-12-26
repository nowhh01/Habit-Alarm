package com.gl.habitalarm.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.HabitRepository
import com.gl.habitalarm.data.HabitWithRepetition
import com.gl.habitalarm.data.RepetitionRepository
import java.time.LocalDate

class HabitViewModel @ViewModelInject constructor(
    private val mHabitRepository: HabitRepository,
    private val mRepetitionRepository: RepetitionRepository
) : ViewModel() {
    private val mToday = LocalDate.now()
    private val mDate = MutableLiveData(mToday)
    val date: LiveData<LocalDate>
        get() = mDate

    private val mHabitsWithRepetitions: LiveData<List<HabitWithRepetition>> =
        Transformations.switchMap(mDate) { date ->
            mHabitRepository.getHabitsWithRepetitionsByDate(date).asLiveData()
        }
    val habitsWithRepetitions
        get() = mHabitsWithRepetitions

    fun changeToPreviousDate() {
        val previous = mDate.value?.minusDays(1)
        mDate.value = previous
    }

    fun changeToNextDate() {
        if(mToday.isAfter(mDate.value)) {
            val next = date.value?.plusDays(1)
            mDate.value = next
        }
    }
}