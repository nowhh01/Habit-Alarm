package com.gl.habitalarm.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MonthValueFormatter(year: Int) : ValueFormatter() {
    private val months = arrayOf(
        "Jan\n$year", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    override fun getFormattedValue(value: Float): String {
        return months[value.toInt()]
    }
}