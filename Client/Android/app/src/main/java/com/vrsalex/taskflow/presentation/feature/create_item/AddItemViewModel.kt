package com.vrsalex.taskflow.presentation.feature.create_item

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.note.NoteRepository
import com.vrsalex.taskflow.domain.item.task.TaskCreate
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.workscape.area.AreaCreate
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.workscape.tag.TagCreate
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import com.vrsalex.taskflow.presentation.feature.create_item.components.SelectorType
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventViewModel
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskContract
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskViewModel
import com.vrsalex.taskflow.presentation.feature.workspace.area.toUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.toUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class AddItemViewModel(
    private val areaRepository: AreaRepository,
    private val tagRepository: TagRepository,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
) : ViewModel() {

    val eventVm = AddItemEventViewModel()
    val taskVm = AddItemTaskViewModel()

    private val _effects = Channel<AddItemBaseContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _formState = MutableStateFlow(AddItemBaseContract.State())

    val state = combine(
        _formState,
        areaRepository.get(),
        tagRepository.get(),
    ) { form, areas, tags ->
        form.copy(
            availableAreas = areas.map { it.toUiModel() },
            availableTags = tags.map { it.toUiModel() }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AddItemBaseContract.State())

    fun onAction(action: AddItemBaseContract.Action) {
        when (action) {
            is AddItemBaseContract.Action.TitleChanged ->
                _formState.update { it.copy(title = action.title) }

            is AddItemBaseContract.Action.DescriptionChanged ->
                _formState.update { it.copy(description = action.description) }

            is AddItemBaseContract.Action.TypeChanged -> _formState.update {
                it.copy(type = action.type)
            }

            is AddItemBaseContract.Action.AreaChanged -> _formState.update {
                it.copy(selectedArea = action.area, activeSelector = SelectorType.NONE, selectorSearch = "")
            }

            is AddItemBaseContract.Action.TagToggled -> _formState.update { state ->
                val isSelected = state.selectedTags.any { it.id == action.tag.id }
                state.copy(
                    selectedTags = if (isSelected)
                        state.selectedTags.filter { it.id != action.tag.id }
                    else
                        state.selectedTags + action.tag
                )
            }

            is AddItemBaseContract.Action.ShowSelector -> _formState.update {
                if (it.activeSelector == action.type)
                    it.copy(activeSelector = SelectorType.NONE)
                else
                    it.copy(activeSelector = action.type, selectorSearch = "")
            }

            is AddItemBaseContract.Action.HideSelector ->
                _formState.update { it.copy(activeSelector = SelectorType.NONE, selectorSearch = "") }

            is AddItemBaseContract.Action.SelectorSearchChanged ->
                _formState.update { it.copy(selectorSearch = action.query) }

            is AddItemBaseContract.Action.SelectorCreate -> viewModelScope.launch {
                when (_formState.value.activeSelector) {
                    SelectorType.TAGS -> {
                        _formState.update { it.copy(selectorSearch = "") }
                        tagRepository.create(TagCreate(name = action.name))
                    }
                    SelectorType.AREA -> {
                        _formState.update { it.copy(selectorSearch = "") }
                        areaRepository.create(AreaCreate(name = action.name))
                    }
                    SelectorType.NONE -> {}
                }
            }
        }
    }

    fun save() {
        val form = _formState.value
        if (form.title.isBlank()) return

        viewModelScope.launch {
            val base = ItemCreate(
                name = form.title,
                description = form.description.ifBlank { null },
                status = ItemStatus.ACTIVE,
                type = form.type,
                priority = 0, // TODO
                areaId = form.selectedArea?.id,
                tagIds = form.selectedTags.map { it.id }
            )

            when (form.type) {
                ItemType.EVENT -> saveEvent(base, eventVm.state.value)
                ItemType.TASK -> saveTask(base, taskVm.state.value)
                ItemType.NOTE -> saveNote(base)
            }

            reset()
//            _effects.send(AddItemBaseContract.Effect.Dismiss)
        }
    }

    fun resumeSheet() {
        viewModelScope.launch { _effects.send(AddItemBaseContract.Effect.ResumeSheet) }
    }


    private suspend fun saveNote(base: ItemCreate){
        noteRepository.create(base)
    }


    private suspend fun saveTask(base: ItemCreate, task: AddItemTaskContract.State) {
        taskRepository.create(
            TaskCreate(
                base = base,
                dueDate = task.dueDate ?: return,
                dueTime = task.time,
                recurrence = null
            )
        )
    }

    private suspend fun saveEvent(base: ItemCreate, event: AddItemEventContract.State) {
        val startDate = event.startDateTime ?: return
        val endDate = event.endDateTime ?: return
        if (startDate > endDate) return
        eventRepository.create(
            EventCreate(
                base = base,
                startDate = startDate.toInstant(TimeZone.currentSystemDefault()),
                endDate = endDate.toInstant(TimeZone.currentSystemDefault()),
                isAllDay = event.isAllDay,
                location = event.location?.ifBlank { null }
            )
        )
    }

    private fun reset() {
        _formState.value = AddItemBaseContract.State()
        eventVm.reset()
        taskVm.reset()
    }

}
