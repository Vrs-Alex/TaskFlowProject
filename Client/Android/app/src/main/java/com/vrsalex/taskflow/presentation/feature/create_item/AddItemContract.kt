package com.vrsalex.taskflow.presentation.feature.create_item

import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.TagUiModel
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.Uuid


object AddItemContract {
    data class State(
        val activeSelector: SelectorType = SelectorType.NONE,
        val selectorSearch: String = "",

        val title: String = "",
        val description: String = "",
        val type: ItemType = ItemType.TASK,

        val availableAreas: List<AreaUiModel> = emptyList(),
        val selectedArea: AreaUiModel? = null,
        val availableTags: List<TagUiModel> = emptyList(),
        val selectedTags: List<TagUiModel> = emptyList(),

        val subItemData: SubItemData? = null
    ) {

        val filteredSelectorItems: List<SelectorItem>
            get() = when (activeSelector) {
                SelectorType.TAGS -> availableTags
                    .filter { it.name.contains(selectorSearch, ignoreCase = true) }
                    .map { SelectorItem(it.id, it.name, it.color) }
                SelectorType.AREA -> availableAreas
                    .filter { it.name.contains(selectorSearch, ignoreCase = true) }
                    .map { SelectorItem(it.id, it.name, it.color) }
                SelectorType.NONE -> emptyList()
            }

        val selectedSelectorIds: Set<Uuid>
            get() = when (activeSelector) {
                SelectorType.TAGS -> selectedTags.map { it.id }.toSet()
                SelectorType.AREA -> setOfNotNull(selectedArea?.id)
                SelectorType.NONE -> emptySet()
            }
    }

    sealed interface Action {
        data class ShowSelector(val type: SelectorType) : Action
        data object HideSelector : Action
        data class SelectorSearchChanged(val query: String) : Action
        data class SelectorCreate(val name: String): Action


        data class TitleChanged(val title: String) : Action
        data class DescriptionChanged(val description: String) : Action
        data class TypeChanged(val type: ItemType) : Action
        data class AreaChanged(val area: AreaUiModel?) : Action
        data class TagToggled(val tag: TagUiModel) : Action

        sealed interface EventAction : Action {
            data class StartDateTimeChanged(val dateTime: LocalDateTime?) : EventAction
            data class EndDateTimeChanged(val dateTime: LocalDateTime?) : EventAction
            data class IsAllDayChanged(val isAllDay: Boolean) : EventAction
            data class LocationChanged(val location: String?) : EventAction
        }

        sealed interface TaskAction : Action {
            data class DueDateChanged(val dateTime: LocalDateTime?) : TaskAction
            data class PriorityChanged(val priority: Int) : TaskAction
        }

        data object Save : Action
    }

    sealed interface SubItemData {
        data class Event(
            val startDateTime: LocalDateTime? = null,
            val endDateTime: LocalDateTime? = null,
            val isAllDay: Boolean = false,
            val location: String? = null,
        ) : SubItemData

        data class Task(
            val dueDate: LocalDateTime? = null,
            val priority: Int = 0,
        ) : SubItemData
    }
}