package com.gl.habitalarm.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.gl.habitalarm.R
import com.gl.habitalarm.data.Habit
import com.gl.habitalarm.data.HabitWithRepetitions
import com.gl.habitalarm.data.Repetition
import com.gl.habitalarm.utils.MonthValueFormatter
import com.gl.habitalarm.utils.LineXAxisRenderer
import com.gl.habitalarm.utils.PercentValueFormatter
import com.gl.habitalarm.utils.WeekValueFormatter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*

private const val TAG = "HabitLineChart"

class HabitLineChart : LineChart {
    companion object {
        private val DEFAULT_LAST_DAY_OF_WEEK = DayOfWeek.SATURDAY
        private val DEFAULT_TEXT_COLOR_ID: Int = R.color.grey_500
        private val PRIMARY_COLOR_ID: Int = R.color.pink_300
        private val MAX_NUM_WEEKS_IN_MONTH = 6
        private val LABEL_COUNT = 12
    }

    private var mbMonth = false
    private var mHabit: Habit? = null
    private var mRepetitions: List<Repetition>? = null
    private var mLineDataMonth: LineData? = null
    private var mLineDataWeek: LineData? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun init() {
        Log.d(TAG, "init(): called")

        super.init()

        setXAxisRenderer(
            LineXAxisRenderer(
                viewPortHandler,
                xAxis,
                getTransformer(YAxis.AxisDependency.LEFT)
            )
        )
        mDescription.isEnabled = false
        setTouchEnabled(false)
        setDrawGridBackground(false)
        mXAxis.labelCount = LABEL_COUNT
        mXAxis.position = XAxis.XAxisPosition.BOTTOM
        mXAxis.setDrawGridLines(false)
        mXAxis.textColor = context.getColor(DEFAULT_TEXT_COLOR_ID)
        mXAxis.granularity = 1f
        mAxisLeft.axisMaximum = 100f
        mAxisLeft.axisMinimum = 0f
        mAxisLeft.valueFormatter = PercentValueFormatter()
        mAxisLeft.textColor = context.getColor(DEFAULT_TEXT_COLOR_ID)
        mAxisRight.isEnabled = false
    }

    fun changeToMonth(bMonth: Boolean) {
        Log.d(TAG, "changeToMonth(): called with $bMonth")

        mbMonth = bMonth
        compute()
    }

    fun setHabitAndRepetitions(habitWithRepetitions: HabitWithRepetitions) {
        Log.d(TAG, "setHabitAndRepetitions(): called with habit ${habitWithRepetitions.habit.name}, ${habitWithRepetitions.repetitions.size} repetitions")

        mHabit = habitWithRepetitions.habit
        mRepetitions = habitWithRepetitions.repetitions
        compute()
    }

    private fun compute() {
        Log.d(TAG, "compute(): called")

        mHabit?.let {
            if (mbMonth) {
                computeEntryMonths(YearMonth.now())
            } else {
                computeEntryWeeks(LocalDate.now())
            }
        }
    }

    private fun computeEntryWeeks(date: LocalDate) {
        Log.d(TAG, "computeEntryWeeks(): called with $date")

        if (mLineDataWeek == null) {
            val entries: MutableList<Entry> = ArrayList()
            val dayOfWeeks: MutableList<DayOfWeek> = ArrayList()
            for (i in 1..7) {
                if (mHabit?.days!![i % 7]) {
                    dayOfWeeks.add(DayOfWeek.of(i))
                }
            }

            // find days of week in each week
            for (week in LABEL_COUNT downTo 1) {
                val localDate = date.minusWeeks(week.toLong())
                val datesInWeek = findDatesInWeek(
                    dayOfWeeks,
                    localDate,
                    null,
                    true
                )
                var percent = 0f
                mRepetitions?.let { repetitions ->
                    var repetitionCountInWeek = 0
                    for (i in repetitions.indices) {
                        val repetition: Repetition = repetitions[i]
                        if (datesInWeek.contains(repetition.date)) {
                            repetitionCountInWeek++
                        }
                    }
                    percent = repetitionCountInWeek.toFloat() / datesInWeek.size * 100
                }

                val xValue = LABEL_COUNT.toFloat() - week
                val entryWeek = Entry(xValue, percent)
                entries.add(entryWeek)
            }
            mLineDataWeek = createDefaultLineData(entries)
        }
        data = mLineDataWeek

        val today = LocalDate.now()
        val dates: MutableList<LocalDate> = ArrayList()
        for (week in LABEL_COUNT downTo 1) {
            val localDates = today.minusWeeks(week.toLong()).with(DEFAULT_LAST_DAY_OF_WEEK)
            dates.add(localDates)
        }
        mXAxis.valueFormatter = WeekValueFormatter(dates)

        invalidate() //refresh
    }

    private fun computeEntryMonths(yearMonth: YearMonth) {
        Log.d(TAG, "computeEntryMonths(): called with $yearMonth")

        if (mLineDataMonth == null) {
            val entries: MutableList<Entry> = ArrayList()
            val dayOfWeeks: MutableList<DayOfWeek> = ArrayList()
            for (i in 1..7) {
                if (mHabit?.days!![i % 7]) {
                    dayOfWeeks.add(DayOfWeek.of(i))
                }
            }

            // find days of week in each month
            val year = yearMonth.year
            for (month in 1..12) {
                val monthInYear = YearMonth.of(year, Month.of(month))
                val datesInMonth = findDatesInMonth(monthInYear, dayOfWeeks)
                var percent = 0f
                mRepetitions?.let { repetitions ->
                    var repetitionCountInMonth = 0
                    for (i in repetitions.indices) {
                        val repetition: Repetition = repetitions[i]
                        if (datesInMonth.contains(repetition.date)) {
                            repetitionCountInMonth++
                        }
                    }
                    percent = repetitionCountInMonth.toFloat() / datesInMonth.size * 100
                }

                val xValue = month.toFloat() - 1f
                val entryMonth = Entry(xValue, percent)
                entries.add(entryMonth)
            }
            mLineDataMonth = createDefaultLineData(entries)
        }
        data = mLineDataMonth
        mXAxis.valueFormatter = MonthValueFormatter(yearMonth.year)

        invalidate() //refresh
    }

    private fun findDatesInMonth(
        monthYear: YearMonth,
        dayOfWeeks: List<DayOfWeek>
    ): List<LocalDate> {
        Log.d(TAG, "findDatesInMonth(): called with $monthYear, ${dayOfWeeks.size} dayOfWeeks")

        val datesInMonth: MutableList<LocalDate> = ArrayList()
        val month = monthYear.month
        for (week in 1..MAX_NUM_WEEKS_IN_MONTH) {
            val randomDateInWeek = monthYear.atDay(1).plusWeeks(week - 1.toLong())
            val datesInWeek = findDatesInWeek(dayOfWeeks, randomDateInWeek, month, false)
            datesInMonth.addAll(datesInWeek)
        }

        Log.d(TAG, "findDatesInMonth(): returns $datesInMonth")
        return datesInMonth
    }

    private fun findDatesInWeek(
        dayOfWeeks: List<DayOfWeek>,
        dateInWeek: LocalDate,
        month: Month?,
        isDateInDifferentMonthIncluded: Boolean
    ): List<LocalDate> {
        Log.d(TAG, "findDatesInWeek(): called with ${dayOfWeeks.size} dayOfWeeks, $dateInWeek, $month, $isDateInDifferentMonthIncluded")

        val dates: MutableList<LocalDate> = ArrayList()
        for (i in dayOfWeeks.indices) {
            val date = dateInWeek.with(dayOfWeeks[i])
            if (!isDateInDifferentMonthIncluded && date.month != month) {
                continue
            }
            dates.add(date)
        }

        Log.d(TAG, "findDatesInWeek(): returns ${dates.size}")
        return dates
    }

    private fun createDefaultLineData(entries: List<Entry>): LineData {
        Log.d(TAG, "createDefaultLineData(): called with ${entries.size} entries")

        val lineDataSet = LineDataSet(entries, "Line Chart")
        lineDataSet.color = context.getColor(PRIMARY_COLOR_ID)
        lineDataSet.lineWidth = 3f
        lineDataSet.circleRadius = 5f
        lineDataSet.setCircleColor(context.getColor(PRIMARY_COLOR_ID))
        lineDataSet.setDrawValues(false)
        lineDataSet.label = ""
        lineDataSet.form = Legend.LegendForm.NONE

        return LineData(lineDataSet)
    }
}