package com.vrsalex.taskflow.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.presentation.feature.event.toUiModel
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.ItemBottomSheetDestination
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.ItemBottomSheetRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Clock

class HomeViewModel(
    eventRepository: EventRepository,
    private val itemBottomSheetRouter: ItemBottomSheetRouter
): ViewModel() {

    private val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM"))
    private val isOpenedCreateBottomSheet = MutableStateFlow(false)

    private val selectedFilterChip = MutableStateFlow(HomeContact.FilterChip.ALL)

    val state = combine(
        eventRepository.getByDate(Clock.System.now()),
        selectedFilterChip,
        isOpenedCreateBottomSheet
    ){ events, selectedFilter, isOpenedCreate ->
        HomeContact.State(
            isOpenCreateBottomSheet = isOpenedCreate,
            todayDate = todayDate,
            selectedFilterChip = selectedFilter,
            eventList = events.map { it.toUiModel() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeContact.State()
    )


    fun onAction(action: HomeContact.Action){
        when(action){
            is HomeContact.Action.IsOpenCreateBottomSheet -> {
                isOpenedCreateBottomSheet.update { action.isOpen }
            }
            is HomeContact.Action.FilterChipSelected -> {
                selectedFilterChip.update { action.chip }
            }
            is HomeContact.Action.EventClicked -> {
                itemBottomSheetRouter.navigate(ItemBottomSheetDestination.EventDetail(action.event.event.base.id))
            }
        }
    }

}