package com.vrsalex.taskflow.presentation.common.extension

import androidx.annotation.StringRes
import com.vrsalex.taskflow.R
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

private val WEEKDAY_NARROW = intArrayOf(
    R.string.weekday_mon_narrow, R.string.weekday_tue_narrow, R.string.weekday_wed_narrow,
    R.string.weekday_thu_narrow, R.string.weekday_fri_narrow, R.string.weekday_sat_narrow,
    R.string.weekday_sun_narrow,
)

private val WEEKDAY_SHORT = intArrayOf(
    R.string.weekday_mon_short, R.string.weekday_tue_short, R.string.weekday_wed_short,
    R.string.weekday_thu_short, R.string.weekday_fri_short, R.string.weekday_sat_short,
    R.string.weekday_sun_short,
)

private val WEEKDAY_FULL = intArrayOf(
    R.string.weekday_mon_full, R.string.weekday_tue_full, R.string.weekday_wed_full,
    R.string.weekday_thu_full, R.string.weekday_fri_full, R.string.weekday_sat_full,
    R.string.weekday_sun_full,
)

@StringRes fun LocalDate.weekdayNarrowRes(): Int = WEEKDAY_NARROW[dayOfWeek.ordinal]

@StringRes fun LocalDate.weekdayShortRes(): Int = WEEKDAY_SHORT[dayOfWeek.ordinal]

@StringRes fun LocalDate.weekdayFullRes(): Int = WEEKDAY_FULL[dayOfWeek.ordinal]

@StringRes fun DayOfWeek.weekdayNarrowRes(): Int = WEEKDAY_NARROW[ordinal]

@StringRes fun DayOfWeek.weekdayShortRes(): Int = WEEKDAY_SHORT[ordinal]
