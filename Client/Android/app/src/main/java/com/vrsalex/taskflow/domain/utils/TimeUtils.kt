package com.vrsalex.taskflow.domain.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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