package com.vrsalex.taskflow.domain.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

/** Компактное отображение даты для чипа: "15 июн" (allDay) или "15 июн, 10:00". */
fun LocalDateTime.formatForChip(allDay: Boolean): String {
    val pattern = if (allDay) "d MMM" else "d MMM, HH:mm"
    return toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern))
}
