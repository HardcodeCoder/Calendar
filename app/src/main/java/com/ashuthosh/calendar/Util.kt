package com.ashuthosh.calendar

import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

fun findDaysAndStartDay(year: Int, month: Int): Pair<Int, Int> {
    val yearMonth = YearMonth.of(year, month)
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    return Pair(yearMonth.lengthOfMonth(), firstDayOfMonth.dayOfWeek.value)
}

fun getMonthText(month: Int) = Month.of(month).name

fun isValidYear(year: Int) = year > 0

fun isValidMonth(month: Int) = month in 1..12