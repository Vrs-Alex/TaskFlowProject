package com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetViewModel
import com.vrsalex.taskflow.presentation.feature.event.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class EventDetailViewModel(
    private val eventId: Uuid,
    private val eventRepository: EventRepository
): ItemBottomSheetViewModel() {

    val event = eventRepository.getById(eventId)
        .map { it?.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)


    override suspend fun onTitleUpdate(title: String) {
        val current = event.value?.event ?: return
        if (title.isBlank()) return
        eventRepository.update(
            EventUpdate(
                base = ItemUpdate(
                    id = current.id,
                    serverId = current.serverId,
                    version = current.version,
                    name = OptionalField.Defined(title)
                )
            )
        )
    }

    override suspend fun onDescriptionUpdate(description: String?) {
        val current = event.value?.event ?: return
        eventRepository.update(
            EventUpdate(
                base = ItemUpdate(
                    id = current.id,
                    serverId = current.serverId,
                    version = current.version,
                    description = OptionalField.Defined(description)
                )
            )
        )
    }

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