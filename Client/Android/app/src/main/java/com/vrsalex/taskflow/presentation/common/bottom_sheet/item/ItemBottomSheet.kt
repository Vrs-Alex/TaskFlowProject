@file:OptIn(ExperimentalMaterial3Api::class)

package com.vrsalex.taskflow.presentation.common.bottom_sheet.item

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event.BottomSheetEventContent
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event.EventDetailViewModel
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task.BottomSheetTaskContent
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task.TaskDetailViewModel
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

    fun onClose() {
        scope.launch {
            sheetState.hide()
            router.dismiss()
        }
    }

    bottomSheetDestination?.let { dest ->
        AppBottomSheet(
            onDismissRequest = { router.dismiss() },
            sheetState = sheetState
        ) {
            when(dest) {
                is ItemBottomSheetDestination.EventDetail -> {
                    val viewModel = koinViewModel<EventDetailViewModel>(key = dest.eventId.toString(), parameters = { parametersOf(dest.eventId) })
                    val eventUi by viewModel.event.collectAsStateWithLifecycle()
                    eventUi?.let { event ->
                        BottomSheetEventContent(
                            eventUi = event,
                            viewModel = viewModel,
                            onClose = { onClose() }
                        )
                    }
                }

                is ItemBottomSheetDestination.TaskDetail -> {
                    val viewModel = koinViewModel<TaskDetailViewModel>(key = dest.taskId.toString(), parameters = { parametersOf(dest.taskId) })
                    val eventUi by viewModel.task.collectAsStateWithLifecycle()
                    eventUi?.let { event ->
                        BottomSheetTaskContent(
                            taskUi = event,
                            viewModel = viewModel,
                            onClose = { onClose() }
                        )
                    }
                }
            }
        }
    }

}