package com.gl.habitalarm.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.HabitRepository
import com.gl.habitalarm.data.HabitWithRepetition
import com.gl.habitalarm.data.RepetitionRepository
import java.time.LocalDate

private const val TAG = "HabitViewModel"

class HabitViewModel @ViewModelInject constructor(
    private val mHabitRepository: HabitRepository,
    private val mRepetitionRepository: RepetitionRepository
) : ViewModel() {
    private val mDate = MutableLiveData(LocalDate.now())
    val date: LiveData<String>
        get() = Transformations.map(mDate) { date ->
            Log.d(TAG, "date: Transformations.map(): called")

            val result = if(date == LocalDate.now()) {
                "TODAY"
            } else {
                mDate.value.toString()
            }

            Log.d(TAG, "date: Transformations.map(): returns $result")
            result
        }

    val habitsWithRepetitions: LiveData<List<HabitWithRepetition>>
        get() = Transformations.switchMap(mDate) { date ->
            Log.d(TAG, "habitsWithRepetitions: Transformations.switchMap(): called")

            val result = mHabitRepository.getHabitsWithRepetitionsByDate(date).asLiveData()

            Log.d(TAG, "habitsWithRepetitions: Transformations.switchMap(): returns $result")
            result
        }

    fun changeToPreviousDate() {
        Log.d(TAG, "changeToPreviousDate(): called")

        val previous = mDate.value?.minusDays(1)
        mDate.value = previous

        Log.d(TAG, "changeToPreviousDate(): mDate value changed to $previous")
    }

    fun changeToNextDate() {
        Log.d(TAG, "changeToNextDate(): called")

        val today = LocalDate.now()
        if(today.isAfter(mDate.value)) {
            val next = mDate.value?.plusDays(1)
            mDate.value = next

            Log.d(TAG, "changeToNextDate(): mDate value changed to $next")
        }
    }
}