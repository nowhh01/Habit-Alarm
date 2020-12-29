package com.gl.habitalarm.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.Month

class WeekValueFormatter(private val mWeeks: List<LocalDate>) : ValueFormatter() {
    private var previousMonth: Month? = null
    private var previousYear = 0

    override fun getFormattedValue(value: Float): String {
        var formattedValue: String
        val date = mWeeks[value.toInt()]

        if (value == 0f || previousYear != date.year) {
            previousYear = date.year
            previousMonth = date.month
            formattedValue = """
                ${previousMonth.toString().substring(0, 3)}
                ${date.year}
                """.trimIndent()
        } else if (previousMonth == date.month) {
            formattedValue = date.dayOfMonth.toString()
        } else {
            previousMonth = date.month
            formattedValue = """
                ${previousMonth.toString().substring(0, 3)}
                ${date.dayOfMonth}
                """.trimIndent()
        }

        return formattedValue
    }
}