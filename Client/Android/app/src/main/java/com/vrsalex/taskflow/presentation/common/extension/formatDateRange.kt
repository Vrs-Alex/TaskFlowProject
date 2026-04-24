package com.vrsalex.taskflow.presentation.common.extension

import com.vrsalex.taskflow.domain.item.event.Event
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime


fun Event.formatDateRange(): String {
    val startDateTime = startDate.toLocalDateTime(TimeZone.UTC)
    val endDateTime = endDate.toLocalDateTime(TimeZone.UTC)

    val sameDay = startDateTime.date == endDateTime.date
    val startIsMidnight = startDateTime.hour == 0 && startDateTime.minute == 0
    val endIsMidnight = endDateTime.hour == 0 && endDateTime.minute == 0

    fun LocalDateTime.formatDate() =
        "%02d.%02d.%02d".format(day, month.number, year % 100)

    fun LocalDateTime.formatTime() =
        "%02d:%02d".format(hour, minute)

    return when {
        sameDay && startIsMidnight && endIsMidnight ->
            startDateTime.formatDate()
        sameDay ->
            "${startDateTime.formatDate()} ${startDateTime.formatTime()} - ${endDateTime.formatTime()}"
        else ->
            "${startDateTime.formatDate()} ${startDateTime.formatTime()} | ${endDateTime.formatDate()} ${endDateTime.formatTime()}"
    }
}