package com.vrsalex.taskflow.domain.item.task

import kotlinx.datetime.LocalDate
import vrsalex.shared.api.item.task.RecurrenceTypeDto

enum class RecurrenceType { DAILY, WEEKLY, MONTHLY, YEARLY }

data class Recurrence(
    val type: RecurrenceType,
    val interval: Int = 1,
    val days: Short? = null,
    val endDate: LocalDate? = null,
    val count: Int? = null
)

fun RecurrenceType.toDto(): RecurrenceTypeDto {
    return when(this){
        RecurrenceType.DAILY -> RecurrenceTypeDto.DAILY
        RecurrenceType.WEEKLY -> RecurrenceTypeDto.WEEKLY
        RecurrenceType.MONTHLY -> RecurrenceTypeDto.MONTHLY
        RecurrenceType.YEARLY -> RecurrenceTypeDto.YEARLY
    }
}

fun RecurrenceTypeDto.toDomain(): RecurrenceType {
    return when(this){
        RecurrenceTypeDto.DAILY -> RecurrenceType.DAILY
        RecurrenceTypeDto.WEEKLY -> RecurrenceType.WEEKLY
        RecurrenceTypeDto.MONTHLY -> RecurrenceType.MONTHLY
        RecurrenceTypeDto.YEARLY -> RecurrenceType.YEARLY
    }
}