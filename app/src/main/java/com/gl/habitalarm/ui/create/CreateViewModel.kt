package com.gl.habitalarm.ui.create

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.Habit
import com.gl.habitalarm.data.HabitRepository
import kotlinx.coroutines.launch
import java.time.LocalTime

private const val TAG = "CreateViewModel"

class CreateViewModel @ViewModelInject constructor(
    private val mHabitRepository: HabitRepository
): ViewModel() {
    private val mHabitName = MutableLiveData("")
    val habitName: LiveData<String>
        get() = mHabitName

    private val mbNotificationOn = MutableLiveData(false)
    val isNotificationOn: LiveData<Boolean>
        get() = mbNotificationOn

    private val mHour = MutableLiveData(0)
    val hour: LiveData<Int>
        get() = mHour

    private val mMinute = MutableLiveData(0)
    val minute: LiveData<Int>
        get() = mMinute

    private val mbSaved = MutableLiveData(false)
    val isSaved: LiveData<Boolean>
        get() = mbSaved

    private val mbDayOns = MutableLiveData(BooleanArray(7))
    val isDayOns: LiveData<BooleanArray>
        get() = mbDayOns

    private val mbAllDaysOn: LiveData<Boolean> =
        Transformations.map(mbDayOns) { bDayOns: BooleanArray ->
            Log.d(TAG, "mbAllDaysOn: Transformations.map(): called")

            var bDayOn = false
            for (day in bDayOns) {
                if (day) {
                    bDayOn = true
                    break
                }
            }

            Log.d(TAG, "mbAllDaysOn: Transformations.map(): returns ${!bDayOn}")
            !bDayOn
        }
    val areAllDaysOn: LiveData<Boolean>
        get() = mbAllDaysOn

    fun onDayClick(day: Int) {
        Log.d(TAG, "onDayClick(): called with $day")

        val bDayOns = mbDayOns.value!!
        bDayOns[day] = !bDayOns[day]
        if (checkAllDaysOn(bDayOns)) {
            changeAllDayOnsTo(false)
        } else {
            mbDayOns.value = bDayOns
        }
    }

    fun onAllDaysClick() {
        Log.d(TAG, "onAllDaysClick(): called")

        if (!mbAllDaysOn.value!!) {
            changeAllDayOnsTo(false)
        }
    }

    fun onNotificationClick(bOn: Boolean) {
        Log.d(TAG, "onNotificationClick(): called with $bOn")

        mbNotificationOn.value = bOn
    }

    fun onSaveClick() {
        viewModelScope.launch {
            Log.d(TAG, "onSaveClick(): called")

            if (habitName.value!!.isEmpty()) {
                Log.d(TAG, "onSaveClick(): habitName empty")

                return@launch
            }

            val habit = Habit(
                name = mHabitName.value!!,
                days = mbDayOns.value!!,
                notifyingTime = if (mbNotificationOn.value!!) {
                    LocalTime.of(hour.value!!, minute.value!!)
                } else {
                    null
                }
            )

            mHabitRepository.addHabit(habit)
            Log.d(
                TAG,
                "onSaveClick(): habit with ${habit.name}, ${habit.days}, ${habit.notifyingTime}")

            mbSaved.value = true
        }
    }

    private fun changeAllDayOnsTo(bTrue: Boolean) {
        Log.d(TAG, "changeAllDayOnsTo(): called with $bTrue")

        val bDayOns = mbDayOns.value!!
        for (i in bDayOns.indices) {
            bDayOns[i] = bTrue
        }
        mbDayOns.value = bDayOns
    }

    private fun checkAllDaysOn(bDayOns: BooleanArray): Boolean {
        Log.d(TAG, "checkAllDaysOn(): called with $bDayOns")

        var bAllDaysOn = true
        for (bDayOn in bDayOns) {
            if (!bDayOn) {
                bAllDaysOn = false
                break
            }
        }
        Log.d(TAG, "checkAllDaysOn(): returns $bAllDaysOn")

        return bAllDaysOn
    }
}