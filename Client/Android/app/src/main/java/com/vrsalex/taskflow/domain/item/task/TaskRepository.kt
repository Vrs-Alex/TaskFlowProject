package com.vrsalex.taskflow.domain.item.task

import com.vrsalex.taskflow.domain.item.base.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository : ItemRepository<Task, TaskCreate, TaskUpdate> {

    fun getOverdue(today: LocalDate): Flow<List<Task>>

    fun getByDateRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Task>>>

    suspend fun changeMarkAsDone(data: TaskLogCreate, isDone: Boolean)

}