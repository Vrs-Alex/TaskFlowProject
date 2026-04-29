package com.vrsalex.taskflow.presentation.feature.create_item

import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.TagUiModel
import kotlinx.datetime.LocalDateTime


object AddItemContract {

    data class State(
        val title: String = "",
        val description: String = "",
        val type: ItemType = ItemType.TASK,

        val availableAreas: List<AreaUiModel> = emptyList(),
        val selectedArea: AreaUiModel? = null,
        val availableTags: List<TagUiModel> = emptyList(),
        val selectedTags: List<TagUiModel> = emptyList(),

        // TODO ATTACHMENT, REMINDER

        val subItemData: SubItemData? = null
    )

    sealed interface Action {
        data class TitleChanged(val title: String) : Action
        data class DescriptionChanged(val description: String) : Action
        data class TypeChanged(val type: ItemType) : Action
        data class AreaChanged(val area: AreaUiModel?) : Action
        data class TagToggled(val tag: TagUiModel) : Action

        sealed interface EventAction : Action {
            data class StartDateTimeChanged(val dateTime: LocalDateTime) : EventAction
            data class EndDateTimeChanged(val dateTime: LocalDateTime) : EventAction
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