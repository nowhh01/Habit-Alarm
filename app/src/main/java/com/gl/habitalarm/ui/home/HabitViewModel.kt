package com.gl.habitalarm.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.HabitRepository
import com.gl.habitalarm.data.HabitWithRepetition
import com.gl.habitalarm.data.Repetition
import com.gl.habitalarm.data.RepetitionRepository
import com.gl.habitalarm.enums.ERepetitionState
import kotlinx.coroutines.launch
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

    fun saveOrUpdateRepetition(repetition: Repetition?, habitId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "saveOrUpdateRepetition(): called with habitId $habitId, $isChecked")

            if (isChecked) {
                val date = mDate.value!!
                val state = ERepetitionState.Done
                val newRepetition = Repetition(
                    habitId = habitId,
                    date = date,
                    state = state
                )
                mRepetitionRepository.addRepetition(newRepetition)
                Log.d(
                    TAG,
                    "saveOrUpdateRepetition(): Repetition with habitId $habitId, date $date, state $state saved")

            } else {
                mRepetitionRepository.removeRepetition(repetition!!)
                Log.d(
                    TAG,
                    "saveOrUpdateRepetition(): Repetition with ${repetition.id} removed")
            }
        }
    }
}