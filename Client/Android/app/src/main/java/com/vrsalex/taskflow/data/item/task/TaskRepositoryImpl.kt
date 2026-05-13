package com.vrsalex.taskflow.data.item.task

import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.taskflow.data.item.base.BaseItemRepositoryImpl
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.item.task.Recurrence
import com.vrsalex.taskflow.domain.item.task.RecurrenceType
import com.vrsalex.taskflow.domain.item.task.Task
import com.vrsalex.taskflow.domain.item.task.TaskCreate
import com.vrsalex.taskflow.domain.item.task.TaskLogCreate
import com.vrsalex.taskflow.domain.item.task.TaskLogRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import vrsalex.shared.api.item.task.TaskDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskRepositoryImpl(
    private val taskApi: TaskApi,
    private val taskLocalDataSource: TaskLocalDataSource,
    itemLocalDataSource: ItemLocalDataSource,
    private val taskLogRepository: TaskLogRepository,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : BaseItemRepositoryImpl<TaskDto, TaskCreate, TaskUpdate, Task>(
    syncEntity = SyncDbEntity.TASK,
    api = taskApi,
    itemLocalDataSource = itemLocalDataSource,
    outboxHandler = outboxHandler,
    syncHandler = syncHandler,
), TaskRepository {

    override suspend fun outboxCreate(id: Uuid): Resource<SyncModel> {
        val task = taskLocalDataSource.getTaskByIdRaw(id)
            ?: return Resource.Failure.Error("Task not found")
        return taskApi.create(task.toDomain().toCreateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun outboxUpdate(id: Uuid): Resource<SyncModel> {
        val task = taskLocalDataSource.getTaskByIdRaw(id)
            ?: return Resource.Failure.Error("Task not found")
        return taskApi.update(task.toDomain().toUpdateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun insert(dto: TaskDto) {
        taskLocalDataSource.insert(
            item = dto.base.toEntityWithRelations(),
            task = dto.toEntity()
        )
    }

    override suspend fun localInsert(data: TaskCreate): Uuid {
        taskLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            task = data.toEntity()
        )
        return data.base.id
    }

    override suspend fun localUpdate(data: TaskUpdate): Uuid {
        taskLocalDataSource.update(data)
        return data.base.id
    }

    override fun observeAll(): Flow<List<Task>> =
        taskLocalDataSource.getTasks().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Task?> =
        taskLocalDataSource.getTask(id).map { it?.toDomain() }

    override fun getByDate(date: Instant): Flow<List<Task>> {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return taskLocalDataSource.getTasksByDate(localDate)
            .map { list ->
                list.filter { relation ->
                    val recurrence = relation.task.toRecurrence()
                    recurrence == null || localDate.matchesRecurrence(recurrence, relation.task.dueDate)
                }.map { it.toDomain(forDate = localDate) }
            }
    }

    override fun getByDateRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Task>>> =
        taskLocalDataSource.getTasksInRange(from, to)
            .map { list ->
                buildMap<LocalDate, MutableList<Task>> {
                    list.forEach { relation ->
                        val recurrence = relation.task.toRecurrence()
                        if (recurrence == null) {
                            getOrPut(relation.task.dueDate) { mutableListOf() }
                                .add(relation.toDomain())
                        } else {
                            var date = maxOf(from, relation.task.dueDate)
                            val end = recurrence.endDate?.let { minOf(it, to) } ?: to
                            while (date <= end) {
                                if (date.matchesRecurrence(recurrence, relation.task.dueDate)) {
                                    getOrPut(date) { mutableListOf() }
                                        .add(relation.toDomain(forDate = date))
                                }
                                date = date.plus(1, DateTimeUnit.DAY)
                            }
                        }
                    }
                }
            }

    override fun getOverdue(today: LocalDate): Flow<List<Task>> =
        taskLocalDataSource.getOverdueCandidates(today)
            .map { list ->
                list.mapNotNull { relation ->
                    val recurrence = relation.task.toRecurrence()
                    if (recurrence == null) {
                        relation.toDomain()
                    } else {
                        val overdueDate = latestOccurrenceBefore(today, relation.task.dueDate, recurrence)
                            ?: return@mapNotNull null
                        val loggedDates = relation.taskLogs
                            .filter { !it.isDeleted }
                            .map { it.date }
                            .toSet()
                        if (overdueDate !in loggedDates) relation.toDomain(forDate = overdueDate) else null
                    }
                }
            }

    override suspend fun changeMarkAsDone(data: TaskLogCreate, isDone: Boolean) {
        if (isDone) taskLogRepository.markAsDone(data)
        else taskLogRepository.markAsUndone(data.id)
    }

    private fun latestOccurrenceBefore(
        today: LocalDate,
        startDate: LocalDate,
        recurrence: Recurrence,
    ): LocalDate? {
        val yesterday = today.minus(1, DateTimeUnit.DAY)
        val effectiveEnd = recurrence.endDate?.let { minOf(it, yesterday) } ?: yesterday
        if (effectiveEnd < startDate) return null
        val unit = when (recurrence.type) {
            RecurrenceType.DAILY -> DateTimeUnit.DAY
            RecurrenceType.WEEKLY -> DateTimeUnit.WEEK
            RecurrenceType.MONTHLY -> DateTimeUnit.MONTH
            RecurrenceType.YEARLY -> DateTimeUnit.YEAR
        }
        val steps = startDate.until(effectiveEnd, unit) / recurrence.interval
        return startDate.plus(steps * recurrence.interval, unit)
    }

    private fun LocalDate.matchesRecurrence(recurrence: Recurrence, startDate: LocalDate): Boolean {
        if (this < startDate) return false
        recurrence.endDate?.let { if (this > it) return false }
        return when (recurrence.type) {
            RecurrenceType.DAILY ->
                startDate.until(this, DateTimeUnit.DAY) % recurrence.interval == 0L
            RecurrenceType.WEEKLY ->
                startDate.until(this, DateTimeUnit.WEEK) % recurrence.interval == 0L &&
                        this.dayOfWeek == startDate.dayOfWeek
            RecurrenceType.MONTHLY ->
                startDate.until(this, DateTimeUnit.MONTH) % recurrence.interval == 0L &&
                        this.dayOfMonth == startDate.dayOfMonth
            RecurrenceType.YEARLY ->
                startDate.until(this, DateTimeUnit.YEAR) % recurrence.interval == 0L &&
                        this.dayOfMonth == startDate.dayOfMonth && this.month == startDate.month
        }
    }
}
