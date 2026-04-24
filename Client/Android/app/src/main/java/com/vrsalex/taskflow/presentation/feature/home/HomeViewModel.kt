package com.vrsalex.taskflow.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Clock

class HomeViewModel(
    private val eventRepository: EventRepository
): ViewModel() {

    val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM"))

    private val selectedFilterChip = MutableStateFlow(HomeContact.FilterChip.ALL)


    val state = combine(
        eventRepository.getByDate(Clock.System.now()),
        selectedFilterChip
    ){ events, selectedFilter ->
        HomeContact.State(
            todayDate = todayDate,
            selectedFilterChip = selectedFilter,
            eventList = events
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeContact.State()
    )


    fun onAction(action: HomeContact.Action){
        when(action){
            is HomeContact.Action.FilterChipSelected -> {
                selectedFilterChip.update { action.chip }
            }
        }
    }


}