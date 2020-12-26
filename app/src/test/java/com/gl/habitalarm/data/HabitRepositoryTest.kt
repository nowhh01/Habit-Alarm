package com.gl.habitalarm.data

import com.gl.habitalarm.enums.EDay
import com.gl.habitalarm.enums.ERepetitionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalTime

class HabitRepositoryTest {
    private val mMockDao = mock(HabitDao::class.java)

    private lateinit var mRepository: HabitRepository

    @Before
    fun setUp() {
        val db = mock(HabitAlarmDatabase::class.java)
        `when`(db.habitDao()).thenReturn(mMockDao)
        mRepository = HabitRepository(db.habitDao())
    }

    @Test
    fun getHabitByName_habit_habitWIthSameValues() = runBlocking {
        val expected = Habit(
            name = "Reading a book",
            days = booleanArrayOf(false, true, true, false, false, false, false),
            notifyingTime = LocalTime.of(1, 2, 3)
        )
        val data = flowOf(expected)
        `when`(mMockDao.getHabitByName(expected.name)).thenReturn(data)

        val result: Habit = mRepository.getHabitByName(expected.name).first()
        verify(mMockDao).getHabitByName(expected.name)

        assertThat(result, `is`(expected))
    }

    @Test
    fun getHabitsWithRepetitionsByDay_habitsAndRepetitions_habitAndRepetitionWithSameValues() =
        runBlocking {
            val provided1 = Habit(
                id = 10,
                name = "Reading a book",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val provided2 = Habit(
                id = 107,
                name = "Workout",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val provided3 = Repetition(
                habitId = provided1.id,
                state = ERepetitionState.Done
            )
            val provided4 = Repetition(
                habitId = provided1.id,
                date = LocalDate.now().minusDays(5),
                state = ERepetitionState.Done
            )

            val expected1 = HabitWithRepetitions(
                habit = provided1,
                repetitions = listOf(provided3, provided4)
            )
            val expected2 = HabitWithRepetitions(
                habit = provided2
            )

            `when`(mMockDao.getHabitsWithRepetitionsByDay(EDay.Monday.ordinal))
                .thenReturn(flowOf(listOf(expected1, expected2)))

            val result = mRepository.getHabitsWithRepetitionsByDay(EDay.Monday).first()
            verify(mMockDao).getHabitsWithRepetitionsByDay(EDay.Monday.ordinal)

            val result1: HabitWithRepetitions = result[0]
            val result2: HabitWithRepetitions = result[1]

            assertThat(result1, `is`(expected1))
            assertThat(result2, `is`(expected2))
        }

    @Test
    fun getHabitsWithRepetitionsByDate_habitsAndRepetitions_habitAndRepetitionWithSameValues() =
        runBlocking {
            val provided1 = Habit(
                id = 10,
                name = "Reading a book",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val provided2 = Habit(
                id = 107,
                name = "Workout",
                days = booleanArrayOf(false, true, true, false, false, false, false),
                notifyingTime = LocalTime.of(1, 2, 3)
            )
            val provided3 = Repetition(
                habitId = provided1.id,
                date = LocalDate.of(2020, 12, 20),
                state = ERepetitionState.Done
            )

            val expected1 = HabitWithRepetition(
                habit = provided1,
                repetition = provided3
            )
            val expected2 = HabitWithRepetition(
                habit = provided2
            )

            val day = EDay.Sunday.ordinal
            val epochDay = provided3.date.toEpochDay()
            `when`(mMockDao.getHabitsWithRepetitionsByDayAndDate(day, epochDay))
                .thenReturn(flowOf(listOf(expected1, expected2)))

            val result = mRepository.getHabitsWithRepetitionsByDate(provided3.date).first()
            verify(mMockDao).getHabitsWithRepetitionsByDayAndDate(day, epochDay)

            val result1: HabitWithRepetition = result[0]
            val result2: HabitWithRepetition = result[1]

            assertThat(result1, `is`(expected1))
            assertThat(result2, `is`(expected2))
        }
}