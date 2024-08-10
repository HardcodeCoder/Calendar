package com.ashuthosh.calendar

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UtilTest {

    @Test
    fun `Given year and month should get its days and start of week`() {
        val result = findDaysAndStartDay(2024, 8)
        assertEquals(31, result.first)
        assertEquals(4, result.second)

        val result2 = findDaysAndStartDay(2024, 7)
        assertEquals(31, result2.first)
        assertEquals(1, result2.second)

        val result3 = findDaysAndStartDay(1250, 4)
        assertEquals(30, result3.first)
        assertEquals(5, result3.second)
    }

    @Test
    fun `Given month as integer should return the corresponding name`() {
        val jan = getMonthText(1)
        assertEquals("JANUARY", jan)

        val dec = getMonthText(12)
        assertEquals("DECEMBER", dec)

        val july = getMonthText(7)
        assertEquals("JULY", july)
    }

    @Test
    fun `Given valid year should return true`() {
        assertEquals(true, isValidYear(2024))
        assertEquals(true, isValidYear(1250))
    }

    @Test
    fun `Given invalid year should return false`() {
        assertEquals(false, isValidYear(0))
        assertEquals(false, isValidYear(-190))
    }

    @Test
    fun `Given valid month should return true`() {
        assertEquals(true, isValidMonth(12))
        assertEquals(true, isValidMonth(1))
    }

    @Test
    fun `Given invalid month should return false`() {
        assertEquals(false, isValidMonth(0))
        assertEquals(false, isValidMonth(-13))
    }
}