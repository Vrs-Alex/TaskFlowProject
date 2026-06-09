package com.vrsalex.taskflow.presentation.feature.add_item.event

import kotlinx.datetime.LocalDateTime

object AddItemEventContract {

    data class State(
        val startDateTime: LocalDateTime? = null,
        val endDateTime: LocalDateTime? = null,
        val isAllDay: Boolean = false,
        val location: String? = null,
    ) {
        val isValid = startDateTime != null && endDateTime != null && startDateTime <= endDateTime
    }

    sealed interface Action {
        data class StartDateTimeChanged(val dateTime: LocalDateTime?) : Action
        data class EndDateTimeChanged(val dateTime: LocalDateTime?) : Action
        data class IsAllDayChanged(val isAllDay: Boolean) : Action
        data class LocationChanged(val location: String?) : Action
    }
}
