package com.vrsalex.taskflow.domain.item.task

import kotlinx.datetime.LocalDate

enum class RecurrenceType { DAILY, WEEKLY, MONTHLY, YEARLY }

data class Recurrence(
    val type: RecurrenceType,
    val interval: Int = 1,
    val days: Short? = null,
    val endDate: LocalDate? = null,
    val count: Int? = null
)