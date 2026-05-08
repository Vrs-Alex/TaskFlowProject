package com.vrsalex.taskflow.domain.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Instant.toDisplayString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy - HH:mm")
    return this.toJavaInstant()
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}


fun Instant.toDisplayDateTime(isAllDay: Boolean): LocalDateTime {
    return if (isAllDay) {
        toLocalDateTime(TimeZone.UTC)
    } else {
        toLocalDateTime(TimeZone.currentSystemDefault())
    }
}


fun LocalDateTime.formatForChip(isAllDay: Boolean): String {
    val currentYear = java.time.Year.now()
    val javaMonth = java.time.Month.of(month.number)
    val monthName = javaMonth.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())
    val year = if (java.time.Year.of(year) == currentYear) "" else " $year"
    val date = "$day $monthName$year"
    return if (isAllDay) date else "$date ${"%02d:%02d".format(hour, minute)}"
}