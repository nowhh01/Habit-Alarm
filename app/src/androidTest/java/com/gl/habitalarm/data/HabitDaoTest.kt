package com.gl.habitalarm.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.gl.habitalarm.enums.EDay
import com.gl.habitalarm.enums.ERepetitionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitDaoTest {
    // Executes each task synchronously using Architecture Components
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var mDatabase: HabitAlarmDatabase
    private lateinit var mHabitDao: HabitDao
    private lateinit var mRepetitionDao: RepetitionDao

    @Before
    fun setUp() {
        mDatabase = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                HabitAlarmDatabase::class.java)
            .build()
        mHabitDao = mDatabase.habitDao()
        mRepetitionDao = mDatabase.repetitionDao()
    }

    @After
    fun tearDown() {
        mDatabase.close()
    }

    @Test
    fun getHabits_habit_habitWithSameValues() = runBlockingTest {
        val expected = Habit(
            name = "Reading a book",
            days = booleanArrayOf(false, true, true, false, false, false, false),
            notifyingTime = LocalTime.of(1, 2, 3)
        )
        mHabitDao.insert(expected)

        val result: Habit = mHabitDao.getHabits().first()[0]

        assertThat(result.name, `is`(expected.name))
        assertThat(result.days, `is`(expected.days))
        assertThat(result.notifyingTime, `is`(expected.notifyingTime))
    }

    @Test
    fun getHabitById_habit_habitWithSameValues() = runBlockingTest {
        val expected = Habit(
            id = 10,
            name = "Reading a book",
            days = booleanArrayOf(false, true, true, false, false, false, false),
            notifyingTime = LocalTime.of(1, 2, 3)
        )
        mHabitDao.insert(expected)

        val result: Habit = mHabitDao.getHabitById(expected.id).first()

        assertThat(result.id, `is`(expected.id))
        assertThat(result.name, `is`(expected.name))
        assertThat(result.days, `is`(expected.days))
        assertThat(result.notifyingTime, `is`(expected.notifyingTime))
    }

    @Test
    fun getHabitByName_habit_habitWithSameValues() = runBlockingTest {
        val expected = Habit(
            name = "Reading a book",
            days = booleanArrayOf(false, true, true, false, false, false, false),
            notifyingTime = LocalTime.of(1, 2, 3)
        )
        mHabitDao.insert(expected)

        val result: Habit = mHabitDao.getHabitByName(expected.name).first()

        assertThat(result.name, `is`(expected.name))
        assertThat(result.days, `is`(expected.days))
        assertThat(result.notifyingTime, `is`(expected.notifyingTime))
    }

    @Test
    fun getHabitByDayOfWeek_habit_habitWithSameValues() = runBlockingTest {
        val expected = Habit(
            name = "Reading a book",
            days = booleanArrayOf(false, true, true, false, false, false, false),
            notifyingTime = LocalTime.of(1, 2, 3)
        )
        mHabitDao.insert(expected)

        val result: Habit = mHabitDao.getHabitsByDay(EDay.Monday.ordinal).first()[0]

        assertThat(result.name, `is`(expected.name))
        assertThat(result.days, `is`(expected.days))
        assertThat(result.notifyingTime, `is`(expected.notifyingTime))
    }

    @Test
    fun getHabitsWithRepetitions_habitAndRepetition_habitAndRepetitionWithSameValues() =
        runBlockingTest {
            val expected1 = Habit(
                id = 10,
                name = "Reading a book",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val expected2 = Repetition(
                habitId = expected1.id,
                state = ERepetitionState.Done
            )
            mHabitDao.insert(expected1)
            mRepetitionDao.insert(expected2)

            val result: HabitWithRepetitions = mHabitDao.getHabitsWithRepetitions().first()[0]
            val result1: Habit = result.habit
            val result2: Repetition = result.repetitions[0]

            assertThat(result1.name, `is`(expected1.name))
            assertThat(result1.days, `is`(expected1.days))
            assertThat(result1.notifyingTime, `is`(expected1.notifyingTime))

            assertThat(result2.habitId, `is`(expected2.habitId))
            assertThat(result2.date, `is`(expected2.date))
            assertThat(result2.state, `is`(expected2.state))
    }

    @Test
    fun getHabitsWithRepetitionsByDayAndDate_habitsAndRepetitions_habitAndRepetitionWithSameValues() =
        runBlockingTest {
            val expected1 = Habit(
                id = 10,
                name = "Reading a book",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val expected2 = Habit(
                id = 107,
                name = "Workout",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val expected3 = Repetition(
                habitId = expected1.id,
                state = ERepetitionState.Done
            )
            val expected4 = Repetition(
                habitId = expected1.id,
                date = LocalDate.now().minusDays(5),
                state = ERepetitionState.Done
            )
            mHabitDao.insert(expected1)
            mHabitDao.insert(expected2)
            mRepetitionDao.insert(expected3)
            mRepetitionDao.insert(expected4)

            val result: List<HabitWithRepetition> =
                mHabitDao.getHabitsWithRepetitionsByDayAndDate(
                    EDay.Monday.ordinal,
                    expected3.date.toEpochDay()
                ).first()
            val result1: Habit = result[0].habit
            val result2: Habit = result[1].habit
            val result3: Repetition? = result[0].repetition
            val result4: Repetition? = result[1].repetition

            assertThat(result1.id, `is`(expected1.id))
            assertThat(result1.name, `is`(expected1.name))
            assertThat(result1.days, `is`(expected1.days))
            assertThat(result1.notifyingTime, `is`(expected1.notifyingTime))

            assertThat(result2.id, `is`(expected2.id))
            assertThat(result2.name, `is`(expected2.name))
            assertThat(result2.days, `is`(expected2.days))
            assertThat(result2.notifyingTime, `is`(expected2.notifyingTime))

            assertThat(result3?.habitId, `is`(expected3.habitId))
            assertThat(result3?.date, `is`(expected3.date))
            assertThat(result3?.state, `is`(expected3.state))

            assertThat(result4, `is`(nullValue()))
        }
}