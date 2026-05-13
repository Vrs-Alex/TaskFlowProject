package com.vrsalex.taskflow.presentation.feature.create_item

import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.feature.create_item.components.SelectorItem
import com.vrsalex.taskflow.presentation.feature.create_item.components.SelectorType
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.TagUiModel
import kotlin.uuid.Uuid

object AddItemBaseContract {

    data class State(
        val title: String = "",
        val description: String = "",
        val type: ItemType = ItemType.NOTE,
        val availableAreas: List<AreaUiModel> = emptyList(),
        val selectedArea: AreaUiModel? = null,
        val availableTags: List<TagUiModel> = emptyList(),
        val selectedTags: List<TagUiModel> = emptyList(),
        val activeSelector: SelectorType = SelectorType.NONE,
        val selectorSearch: String = "",
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
        data class TitleChanged(val title: String) : Action
        data class DescriptionChanged(val description: String) : Action
        data class TypeChanged(val type: ItemType) : Action
        data class AreaChanged(val area: AreaUiModel?) : Action
        data class TagToggled(val tag: TagUiModel) : Action
        data class ShowSelector(val type: SelectorType) : Action
        data object HideSelector : Action
        data class SelectorSearchChanged(val query: String) : Action
        data class SelectorCreate(val name: String) : Action
    }

    sealed interface Effect {
        data object ResumeSheet : Effect
        data object Dismiss : Effect
    }
}
