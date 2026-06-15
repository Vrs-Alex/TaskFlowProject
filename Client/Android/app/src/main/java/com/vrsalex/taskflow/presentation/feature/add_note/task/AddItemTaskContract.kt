package com.vrsalex.taskflow.presentation.feature.add_note.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

object AddItemTaskContract {

    data class State(
        val dueDate: LocalDate? = null,
        val time: LocalTime? = null,
    )

    sealed interface Action {
        data class DueDateChanged(val date: LocalDate?) : Action
        data class TimeChanged(val time: LocalTime?) : Action
    }
}
