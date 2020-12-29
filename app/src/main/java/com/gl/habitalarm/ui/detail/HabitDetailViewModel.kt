package com.gl.habitalarm.ui.detail

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.Habit
import com.gl.habitalarm.data.HabitRepository
import com.gl.habitalarm.data.HabitWithRepetitions
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

private const val TAG = "HabitDetailViewModel"

class HabitDetailViewModel @ViewModelInject constructor(
    @Assisted
    private val mSavedStateHandle: SavedStateHandle,
    private val mHabitRepository: HabitRepository
) : ViewModel() {
    companion object {
        const val INTENT_HABIT_ID = "com.gl.habitalarm.ui.detail.habit_id"
    }

    private val mHabitId = MutableLiveData<Long>(mSavedStateHandle[INTENT_HABIT_ID])
    private val mHabitWithRepetitions: LiveData<HabitWithRepetitions> =
        Transformations.switchMap(mHabitId) { habitId: Long ->
            Log.d(TAG,"mHabitWithRepetitions: Transformations.switchMap(): called with habitId $habitId")

            mHabitRepository.getHabitWithRepetitionsById(habitId).asLiveData()
        }
    val habitWithRepetitions: LiveData<HabitWithRepetitions>
        get() = mHabitWithRepetitions

    private val mHabit: LiveData<Habit> =
        Transformations.map(mHabitWithRepetitions) { habitWithRepetitions: HabitWithRepetitions ->
            Log.d(TAG,"mHabit: Transformations.map(): called with habit ${habitWithRepetitions.habit.name} and ${habitWithRepetitions.repetitions.size}")

            habitWithRepetitions.habit
        }
    val habit: LiveData<Habit>
        get() = mHabit

    private val mNotifyingTime: LiveData<LocalTime> =
        Transformations.map(mHabit) { habit: Habit ->
            Log.d(TAG, "mNotifyingTime: Transformations.map(): called with habit ${habit.name}")

            habit.notifyingTime
        }
    val notifyingTime: LiveData<LocalTime>
        get() = mNotifyingTime

    private val mDays: LiveData<String> =
        Transformations.map(mHabit) { habit: Habit ->
            Log.d(TAG, "mDays: Transformations.map(): called with habit ${habit.name}")

            val days: BooleanArray = habit.days
            var result = if (days[0]) "SUN " else ""
            var isEverydayOn = true
            for (i in 1 until days.size) {
                if (days[i]) {
                    result += DayOfWeek.of(i).toString().substring(0, 3) + ' '
                } else {
                    isEverydayOn = false
                }
            }

            return@map if (isEverydayOn) {
                "EVERYDAY"
            } else {
                result
            }
        }
    val days: LiveData<String>
        get() = mDays

    fun removeHabit() {
        viewModelScope.launch {
            mHabit.value?.let {
                mHabitRepository.removeHabit(it)
            }
        }
    }
}