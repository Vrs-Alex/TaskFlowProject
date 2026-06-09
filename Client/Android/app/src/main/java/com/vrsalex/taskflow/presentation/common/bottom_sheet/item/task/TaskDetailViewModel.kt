package com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task

import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.data.item.task.toTaskLog
import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetViewModel
import com.vrsalex.taskflow.presentation.model.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class TaskDetailViewModel(
    private val taskId: Uuid,
    private val taskRepository: TaskRepository
): ItemBottomSheetViewModel() {

    val task = taskRepository.getById(taskId)
        .map { it?.toUiModel() }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)


    override suspend fun onTitleUpdate(title: String) {
        val current = task.value?.task ?: return
        if (title.isBlank()) return
        taskRepository.update(
            TaskUpdate(
                base = ItemUpdate(
                    id = current.id,
                    serverId = current.serverId,
                    version = current.version,
                    name = OptionalField.Defined(title)
                )
            )
        )
    }

    override suspend fun onDescriptionUpdate(description: String?) {
        val current = task.value?.task ?: return
        taskRepository.update(
            TaskUpdate(
                base = ItemUpdate(
                    id = current.id,
                    serverId = current.serverId,
                    version = current.version,
                    description = OptionalField.Defined(description)
                )
            )
        )
    }

    override fun syncItem(id: Uuid) {
        viewModelScope.launch {
            taskRepository.syncItem(id)
        }
    }

    fun delete(id: Uuid) {
        viewModelScope.launch {
            taskRepository.delete(id)
        }
    }

    fun update(data: TaskUpdate) {
        viewModelScope.launch {
            taskRepository.update(data)
        }
    }

    fun changeMarkAsDone(value: Boolean){
        val current = task.value?.task ?: return
        viewModelScope.launch {
            taskRepository.changeMarkAsDone(
                data = current.toTaskLog(forDate = current.dueDate),
                isDone = value
            )
        }
    }
}