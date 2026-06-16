package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.sync.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface TaskRepository : SyncRepository<Task, TaskCreate, TaskUpdate> {
    fun observeInbox(): Flow<List<Task>>
    fun observeByDate(date: Instant): Flow<List<Task>>
    fun observeOverdue(today: LocalDate): Flow<List<Task>>
    fun observeByDateRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Task>>>
    suspend fun setDone(taskId: Uuid, date: LocalDate, done: Boolean)
}