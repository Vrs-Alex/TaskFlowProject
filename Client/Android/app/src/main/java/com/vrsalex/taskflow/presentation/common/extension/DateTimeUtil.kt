package com.vrsalex.taskflow.presentation.common.extension

import androidx.compose.ui.graphics.vector.Path
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.utils.toDisplayDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.number
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Clock


fun Event.formatDateRange(isOnlyEnd: Boolean = false): String {
    val start = startDate.toDisplayDateTime(isAllDay)
    val end = endDate.toDisplayDateTime(isAllDay)

    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).year

    fun LocalDateTime.formatDate(): String {
        return if (year == currentYear)
            this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("d MMMM"))
        else
            this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
    }

    fun LocalDateTime.formatTime() = "%02d:%02d".format(hour, minute)
    val sameDay = start.date == end.date

    return when {
        isOnlyEnd && isAllDay -> {
            "До " + end.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("d MMMM"))
        }
        isOnlyEnd -> {
            "До " + end.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("d MMMM, HH:mm"))
        }
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