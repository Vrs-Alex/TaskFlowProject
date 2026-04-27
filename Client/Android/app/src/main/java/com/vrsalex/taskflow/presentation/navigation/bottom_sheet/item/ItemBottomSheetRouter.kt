package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.uuid.Uuid


class ItemBottomSheetRouter {

    private val _destination = MutableSharedFlow<ItemBottomSheetDestination?>(extraBufferCapacity = 1)
    val destination = _destination.asSharedFlow()


    fun navigate(destination: ItemBottomSheetDestination) {
        _destination.tryEmit(destination)
    }

    fun dismiss() {
        _destination.tryEmit(null)
    }
}

sealed interface ItemBottomSheetDestination {
    data class EventDetail(val eventId: Uuid) : ItemBottomSheetDestination
}