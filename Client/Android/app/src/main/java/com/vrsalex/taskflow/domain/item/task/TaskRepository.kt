package com.vrsalex.taskflow.domain.item.task

import com.vrsalex.taskflow.domain.item.base.ItemRepository
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

interface TaskRepository : ItemRepository<Task, TaskCreate, TaskUpdate> {

    suspend fun changeMarkAsDone(data: TaskLogCreate, isDone: Boolean)

}