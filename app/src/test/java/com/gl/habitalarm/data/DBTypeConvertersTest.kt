package com.gl.habitalarm.data

import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class DBTypeConvertersTest {
    private lateinit var mConverter: DBTypeConverters

    @Before
    fun setUp() {
        mConverter = DBTypeConverters()
    }

    @Test
    fun BooleanArrayToInt_booleansWith3Trues_11() {
        val provided = booleanArrayOf(
            true, true, false, true, false, false, false
        )
        val expected = 11
        val result: Int = mConverter.booleanArrayToInt(provided)
        assertThat(result, Is.`is`(expected))
    }

    @Test
    fun IntToBooleanArray_11_booleansWith3Trues() {
        val provided = 11
        val expected = booleanArrayOf(
            true, true, false, true, false, false, false
        )
        val result: BooleanArray = mConverter.intToBooleanArray(provided)
        assertThat(result, Is.`is`(expected))
    }
}