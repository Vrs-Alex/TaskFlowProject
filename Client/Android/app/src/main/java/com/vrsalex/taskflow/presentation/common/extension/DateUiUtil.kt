package com.vrsalex.taskflow.presentation.common.extension

import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.utils.toDisplayDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


fun Event.formatDateRange(): String {
    val start = startDate.toDisplayDateTime(isAllDay)
    val end = endDate.toDisplayDateTime(isAllDay)
    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).year

    fun LocalDateTime.formatDate(): String {
        return if (year == currentYear)
            "%02d.%02d".format(day, month.number)
        else
            "%02d.%02d.%02d".format(day, month.number, year % 100)
    }

    fun LocalDateTime.formatTime() = "%02d:%02d".format(hour, minute)

    val sameDay = start.date == end.date

    return when {
        isAllDay && sameDay ->
            start.formatDate()
        isAllDay ->
            "${start.formatDate()} – ${end.formatDate()}"
        sameDay ->
            "${start.formatDate()} · ${start.formatTime()} – ${end.formatTime()}"
        else ->
            "${start.formatDate()} ${start.formatTime()} → ${end.formatDate()} ${end.formatTime()}"
    }
}