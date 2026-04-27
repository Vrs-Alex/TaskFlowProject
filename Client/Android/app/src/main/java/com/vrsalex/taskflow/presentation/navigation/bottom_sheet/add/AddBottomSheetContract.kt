package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.add

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.TagUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.Uuid


object AddBottomSheetContract {
    data class State(
        val title: String = "",
        val description: String = "",
        val type: ItemType = ItemType.TASK,
        val area: AreaUiModel? = null,
        val availableAreas: List<AreaUiModel> = listOf(
            AreaUiModel(id = Uuid.random(), name = "Работа", color = Color(0xFF4A90E2)),
            AreaUiModel(id = Uuid.random(), name = "Личное", color = Color(0xFF2ECC87)),
            AreaUiModel(id = Uuid.random(), name = "Учёба", color = Color(0xFFE6B34A)),
        ),
        val availableTags: List<TagUiModel> = listOf(
            TagUiModel(id = Uuid.random(), name = "Важное", color = Color(0xFFFF6B6B)),
            TagUiModel(id = Uuid.random(), name = "Study", color = Color(0xFF4A90E2)),
            TagUiModel(id = Uuid.random(), name = "Встреча", color = Color(0xFF2ECC87)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
            TagUiModel(id = Uuid.random(), name = "Срочно", color = Color(0xFFE6B34A)),
        ),
        val selectedTags: List<TagUiModel> = emptyList(),



        val startDateTime: LocalDateTime? = null,
        val endDateTime: LocalDateTime? = null,
        val isAllDay: Boolean = false,
        val location: String? = null
    )

    sealed interface Action {
        data class TitleChanged(val title: String) : Action
        data class DescriptionChanged(val description: String) : Action
        data class TypeChanged(val type: ItemType) : Action
        data class AreaChanged(val area: AreaUiModel?) : Action
        data class TagToggled(val tag: TagUiModel) : Action

        data class StartDateTimeChanged(val dateTime: LocalDateTime) : Action
        data class EndDateTimeChanged(val dateTime: LocalDateTime) : Action
        data class IsAllDayChanged(val isAllDay: Boolean) : Action
        data class LocationChanged(val location: String?) : Action


        data object Save : Action
    }


    sealed interface TypeData {
        data class Event(
            val startDateTime: LocalDateTime? = null,
            val endDateTime: LocalDateTime? = null,
            val isAllDay: Boolean = false,
            val location: String? = null,
        ) : TypeData

        data class Task(
            val dueDate: LocalDateTime? = null,
            val priority: Int = 0,
        ) : TypeData

        data class Goal(
            val targetDate: LocalDate? = null,
            val metric: String? = null,
        ) : TypeData

//        data class Habit(
//            val frequency: Frequency = Frequency.DAILY,
//            val reminderTime: LocalTime? = null,
//        ) : TypeData
    }
}