package com.vrsalex.taskflow.presentation.feature.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.common.validation.getOrNull
import com.vrsalex.taskflow.domain.common.validation.note.NoteDescription
import com.vrsalex.taskflow.domain.common.validation.note.NoteName
import com.vrsalex.taskflow.domain.common.validation.worksapce.AreaName
import com.vrsalex.taskflow.domain.common.validation.worksapce.TagName
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.domain.note.event.EventCreate
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.task.TaskCreate
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.sync.SyncModelCreate
import com.vrsalex.taskflow.domain.workspace.area.AreaCreate
import com.vrsalex.taskflow.domain.workspace.area.AreaRepository
import com.vrsalex.taskflow.domain.workspace.tag.TagCreate
import com.vrsalex.taskflow.domain.workspace.tag.TagRepository
import com.vrsalex.taskflow.presentation.feature.add_note.components.SelectorType
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventViewModel
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskContract
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskViewModel
import com.vrsalex.taskflow.presentation.model.toUiModel
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
import kotlin.uuid.Uuid

class AddNoteViewModel(
    private val areaRepository: AreaRepository,
    private val tagRepository: TagRepository,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
) : ViewModel() {

    val eventVm = AddItemEventViewModel()
    val taskVm = AddItemTaskViewModel()

    private val _effects = Channel<AddNoteBaseContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _formState = MutableStateFlow(AddNoteBaseContract.State())

    val state = combine(
        _formState,
        areaRepository.observeAll(),
        tagRepository.observeAll(),
    ) { form, areas, tags ->
        form.copy(
            availableAreas = areas.map { it.toUiModel() },
            availableTags = tags.map { it.toUiModel() }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AddNoteBaseContract.State())

    fun onAction(action: AddNoteBaseContract.Action) {
        when (action) {
            is AddNoteBaseContract.Action.TitleChanged ->
                _formState.update { it.copy(title = action.title) }

            is AddNoteBaseContract.Action.DescriptionChanged ->
                _formState.update { it.copy(description = action.description) }

            is AddNoteBaseContract.Action.TypeChanged ->
                _formState.update { it.copy(type = action.type) }

            is AddNoteBaseContract.Action.AreaChanged -> _formState.update {
                it.copy(selectedArea = action.area, activeSelector = SelectorType.NONE, selectorSearch = "")
            }

            is AddNoteBaseContract.Action.TagToggled -> _formState.update { state ->
                val isSelected = state.selectedTags.any { it.id == action.tag.id }
                state.copy(
                    selectedTags = if (isSelected)
                        state.selectedTags.filter { it.id != action.tag.id }
                    else
                        state.selectedTags + action.tag
                )
            }

            is AddNoteBaseContract.Action.ShowSelector -> _formState.update {
                if (it.activeSelector == action.type)
                    it.copy(activeSelector = SelectorType.NONE)
                else
                    it.copy(activeSelector = action.type, selectorSearch = "")
            }

            is AddNoteBaseContract.Action.HideSelector ->
                _formState.update { it.copy(activeSelector = SelectorType.NONE, selectorSearch = "") }

            is AddNoteBaseContract.Action.SelectorSearchChanged ->
                _formState.update { it.copy(selectorSearch = action.query) }

            is AddNoteBaseContract.Action.SelectorCreate -> viewModelScope.launch {
                when (_formState.value.activeSelector) {
                    SelectorType.TAGS -> {
                        val name = TagName.of(action.name).getOrNull() ?: return@launch
                        _formState.update { it.copy(selectorSearch = "") }
                        tagRepository.create(
                            TagCreate(name = name, color = Color(DEFAULT_COLOR), syncModelCreate = newSync())
                        )
                    }
                    SelectorType.AREA -> {
                        val name = AreaName.of(action.name).getOrNull() ?: return@launch
                        _formState.update { it.copy(selectorSearch = "") }
                        areaRepository.create(
                            AreaCreate(name = name, color = Color(DEFAULT_COLOR), syncModelCreate = newSync())
                        )
                    }
                    SelectorType.NONE -> {}
                }
            }
        }
    }

    fun save() {
        val form = _formState.value
        val name = NoteName.of(form.title).getOrNull() ?: return

        viewModelScope.launch {
            val base = NoteCreate(
                name = name,
                description = form.description.ifBlank { null }?.let { NoteDescription.of(it).getOrNull() },
                type = form.type,
                status = NoteStatus.ACTIVE,
                priority = 0,
                area = form.selectedArea?.area,
                tags = form.selectedTags.map { it.tag },
                syncModelCreate = newSync(),
            )

            when (form.type) {
                NoteType.EVENT -> saveEvent(base, eventVm.state.value)
                NoteType.TASK -> saveTask(base, taskVm.state.value)
                NoteType.NOTE -> noteRepository.create(base)
                else -> {}
            }

            reset()
        }
    }

    fun resumeSheet() {
        viewModelScope.launch { _effects.send(AddNoteBaseContract.Effect.ResumeSheet) }
    }

    private suspend fun saveTask(base: NoteCreate, task: AddItemTaskContract.State) {
        taskRepository.create(
            TaskCreate(
                dueDate = task.dueDate,
                dueTime = task.time,
                recurrenceType = null,
                recurrenceDays = null,
                recurrenceEndDate = null,
                recurrenceCount = null,
                note = base,
            )
        )
    }

    private suspend fun saveEvent(base: NoteCreate, event: AddItemEventContract.State) {
        val start = event.startDateTime ?: return
        val end = event.endDateTime
        if (end != null && start > end) return
        val tz = TimeZone.currentSystemDefault()
        eventRepository.create(
            EventCreate(
                startDate = start.toInstant(tz),
                endDate = end?.toInstant(tz),
                isAllDay = event.isAllDay,
                location = event.location?.ifBlank { null },
                note = base,
            )
        )
    }

    private fun newSync() = SyncModelCreate(id = Uuid.random())

    private fun reset() {
        _formState.value = AddNoteBaseContract.State()
        eventVm.reset()
        taskVm.reset()
    }

    private companion object {
        const val DEFAULT_COLOR = "#808080"
    }
}
