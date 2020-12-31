package com.gl.habitalarm.ui.createoredit

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.gl.habitalarm.data.Habit
import com.gl.habitalarm.data.HabitRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime

private const val TAG = "HabitCreateOrEditViewModel"

class HabitCreateOrEditViewModel @ViewModelInject constructor(
        @Assisted
        private val mSavedStateHandle: SavedStateHandle,
        private val mHabitRepository: HabitRepository
): ViewModel() {
    companion object {
        const val INTENT_HABIT_ID = "com.gl.habitalarm.ui.createoredit.habit_id"
    }

    private val mHabitId = MutableLiveData(0L)

    private val mHabitName = MutableLiveData("")
    val habitName: MutableLiveData<String>
        get() = mHabitName

    private val mbDayOns = MutableLiveData(BooleanArray(7))
    val isDayOns: MutableLiveData<BooleanArray>
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

    private val mbNotificationOn = MutableLiveData(false)
    val isNotificationOn: MutableLiveData<Boolean>
        get() = mbNotificationOn

    private val mHour = MutableLiveData(0)
    val hour: MutableLiveData<Int>
        get() = mHour

    private val mMinute = MutableLiveData(0)
    val minute: MutableLiveData<Int>
        get() = mMinute

    private val mbSaved = MutableLiveData(false)
    val isSaved: LiveData<Boolean>
        get() = mbSaved

    private val mbEdit = MutableLiveData(false)
    val isEdit: LiveData<Boolean>
        get() = mbEdit

    init {
        Log.d(TAG, "init(): called")

        val habitId: Long? = mSavedStateHandle[INTENT_HABIT_ID]
        habitId?.let {
            viewModelScope.launch {
                mHabitRepository.getHabitById(it).collect { habit ->
                    Log.d(TAG, "init(): getHabitById().map(): called with habit ${habit.name}")

                    mHabitId.value = habit.id
                    mHabitName.value = habit.name

                    val days: BooleanArray = habit.days
                    if (checkAllDaysOn(days)) {
                        changeAllDayOnsTo(false)
                    } else {
                        mbDayOns.value = days
                    }

                    habit.notifyingTime?.apply {
                        mbNotificationOn.value = true
                        mHour.value = hour
                        mMinute.value = minute
                    }

                    mbEdit.value = true
                }
            }
        }
    }

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

            if (mbAllDaysOn.value!!) {
                changeAllDayOnsTo(true)
            }

            val habit = Habit(
                    id = mHabitId.value!!,
                    name = mHabitName.value!!,
                    days = mbDayOns.value!!,
                    notifyingTime = if (mbNotificationOn.value!!) {
                        LocalTime.of(hour.value!!, minute.value!!)
                    } else {
                        null
                    }
            )

            if(mbEdit.value!!) {
                mHabitRepository.updateHabit(habit)
                Log.d(TAG, "onSaveClick(): update habit ${habit.id} with ${habit.name}, ${habit.days}, ${habit.notifyingTime}")
            } else {
                mHabitRepository.addHabit(habit)
                Log.d(TAG, "onSaveClick(): add habit with ${habit.name}, ${habit.days}, ${habit.notifyingTime}")
            }

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