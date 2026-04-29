package com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.presentation.feature.event.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class EventDetailViewModel(
    private val eventId: Uuid,
    private val eventRepository: EventRepository
): ViewModel() {

    val event = eventRepository.getById(eventId)
        .map { it?.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun delete(id: Uuid) {
        viewModelScope.launch {
            eventRepository.delete(id)
        }
    }

    fun update(data: EventUpdate) {
        viewModelScope.launch {
            eventRepository.update(data)
        }
    }

}