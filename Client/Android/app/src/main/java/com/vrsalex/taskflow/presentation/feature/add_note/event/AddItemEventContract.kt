package com.vrsalex.taskflow.presentation.feature.add_note.event

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

object AddItemEventContract {

    data class State(
        val startDateTime: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        val endDateTime: LocalDateTime? = null,
        val isAllDay: Boolean = false,
        val location: String? = null,
    ) {
        val isValid = startDateTime != null && (endDateTime == null || endDateTime > startDateTime)
    }

    sealed interface Action {
        data class StartDateTimeChanged(val dateTime: LocalDateTime?) : Action
        data class EndDateTimeChanged(val dateTime: LocalDateTime?) : Action
        data class IsAllDayChanged(val isAllDay: Boolean) : Action
        data class LocationChanged(val location: String?) : Action
    }
}
