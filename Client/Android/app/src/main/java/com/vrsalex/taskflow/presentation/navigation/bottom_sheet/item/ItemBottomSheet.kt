@file:OptIn(ExperimentalMaterial3Api::class)

package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.data.item.event.toUpdateDto
import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.common.model.toOptional
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.event.BottomSheetEventContent
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.event.EventDetailViewModel
import com.vrsalex.uikit.component.modal.AppBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ItemBottomSheet() {

    val router = koinInject<ItemBottomSheetRouter>()
    val bottomSheetDestination by router.destination.collectAsStateWithLifecycle(null)

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    bottomSheetDestination?.let { dest ->
        AppBottomSheet(
            onDismissRequest = { router.dismiss() },
            sheetState = sheetState,
        ) {
            when(dest) {
                is ItemBottomSheetDestination.EventDetail -> {
                    val viewModel = koinViewModel<EventDetailViewModel>(key = dest.eventId.toString(), parameters = { parametersOf(dest.eventId) })
                    val eventUi by viewModel.event.collectAsStateWithLifecycle()
                    eventUi?.let { event ->
                        BottomSheetEventContent(
                            eventUi = event,
                            onClose = {
                                scope.launch {
                                    sheetState.hide()
                                    router.dismiss()
                                }
                            },
                            onEdit = {

                            },
                            onArchive = { status ->
                                scope.launch {
                                    sheetState.hide()
                                    router.dismiss()
                                    viewModel.update(
                                        EventUpdate(
                                            base = ItemUpdate(
                                                id = event.event.id,
                                                serverId = event.event.serverId,
                                                version = event.event.version,
                                                status = status.toOptional()
                                            )
                                        )
                                    )
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    sheetState.hide()
                                    router.dismiss()
                                    viewModel.delete(dest.eventId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}