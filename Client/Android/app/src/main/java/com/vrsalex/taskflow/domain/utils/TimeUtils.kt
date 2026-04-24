package com.vrsalex.taskflow.domain.utils

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