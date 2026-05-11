package com.vrsalex.taskflow.presentation.feature.inbox

import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.workscape.area.Area
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.taskflow.presentation.feature.event.EventUiModel
import kotlin.uuid.Uuid

object InboxContract {

    data class State(
        val selectedTab: Tab = Tab.ITEMS,
        val searchQuery: String = "",
        val events: List<EventUiModel> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val areas: List<Area> = emptyList(),
        val colorEdit: ColorEditState? = null
    )

    data class ColorEditState(
        val itemId: Uuid,
        val currentHex: String,
        val isTag: Boolean
    )

    sealed interface Action {
        data class TabSelected(val tab: Tab) : Action
        data class SearchQueryChanged(val query: String) : Action
        data class EventClicked(val event: EventUiModel) : Action
        data class TagDeleted(val id: Uuid) : Action
        data class AreaDeleted(val id: Uuid) : Action
        data class ColorEditStarted(val itemId: Uuid, val hex: String, val isTag: Boolean) : Action
        data object ColorEditDismissed : Action
        data class TagColorSaved(val id: Uuid, val hex: String) : Action
        data class AreaColorSaved(val id: Uuid, val hex: String) : Action
    }

    enum class Tab(val title: Int) {
        ITEMS(R.string.notes),
        TAGS(R.string.tags),
        AREAS(R.string.areas)
    }
}
