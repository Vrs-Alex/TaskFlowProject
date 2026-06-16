package com.vrsalex.taskflow.presentation.feature.calendar.model

import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import kotlinx.datetime.LocalDate

data class CalendarDayState(
    val date: LocalDate,
    val events: List<EventUiModel> = emptyList()
)