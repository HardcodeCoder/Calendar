package com.ashuthosh.calendar

import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

fun findDaysAndStartDay(year: Int, month: Int): Pair<Int, Int> {
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val startDay = firstDayOfMonth.dayOfWeek.value
    return Pair(daysInMonth, startDay)
}

fun getMonthText(month: Int) = Month.of(month).name