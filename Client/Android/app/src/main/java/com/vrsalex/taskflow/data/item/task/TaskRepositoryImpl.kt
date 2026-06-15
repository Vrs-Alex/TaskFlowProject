package com.vrsalex.taskflow.data.item.task

import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.taskflow.data.item.base.ItemSyncEngine
import com.vrsalex.taskflow.data.item.base.RemoteSync
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.item.task.Recurrence
import com.vrsalex.taskflow.domain.item.task.RecurrenceType
import com.vrsalex.taskflow.domain.item.task.Task
import com.vrsalex.taskflow.domain.item.task.TaskCreate
import com.vrsalex.taskflow.domain.item.task.TaskLogCreate
import com.vrsalex.taskflow.domain.item.task.TaskLogRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskRepositoryImpl(
    taskApi: TaskApi,
    private val taskLocalDataSource: TaskLocalDataSource,
    itemLocalDataSource: ItemLocalDataSource,
    private val taskLogRepository: TaskLogRepository,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : TaskRepository {

    private val engine = ItemSyncEngine(
        syncEntity = SyncDbEntity.TASK,
        api = taskApi,
        itemLocalDataSource = itemLocalDataSource,
        outboxHandler = outboxHandler,
        syncHandler = syncHandler,
        remote = object : RemoteSync<TaskDto, TaskCreateRequest, TaskUpdateRequest> {
            override suspend fun applyRemote(dto: TaskDto) {
                taskLocalDataSource.insert(
                    item = dto.base.toEntityWithRelations(),
                    task = dto.toEntity()
                )
            }

            override suspend fun buildCreate(id: Uuid): TaskCreateRequest? =
                taskLocalDataSource.getTaskByIdRaw(id)?.toDomain()?.toCreateDto()

            override suspend fun buildUpdate(id: Uuid): TaskUpdateRequest? =
                taskLocalDataSource.getTaskByIdRaw(id)?.toDomain()?.toUpdateDto()
        },
    )

    override fun get(): Flow<List<Task>> =
        taskLocalDataSource.getTasks().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Task?> =
        taskLocalDataSource.getTask(id).map { it?.toDomain() }

    override suspend fun create(data: TaskCreate) {
        taskLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            task = data.toEntity()
        )
        engine.enqueueCreate(data.base.id)
    }

    override suspend fun update(data: TaskUpdate) {
        taskLocalDataSource.update(data)
        engine.enqueueUpdate(data.base.id)
    }

    override suspend fun delete(id: Uuid) = engine.delete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = engine.sync(lastSync)

    override suspend fun syncItem(id: Uuid): Resource<Unit> = engine.syncItem(id)

    override fun getByDate(date: Instant): Flow<List<Task>> {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return taskLocalDataSource.getTasksByDate(localDate)
            .map { list ->
                list.filter { relation ->
                    val recurrence = relation.task.toRecurrence()
                    val due = relation.task.dueDate
                    recurrence == null || (due != null && localDate.matchesRecurrence(recurrence, due))
                }.map { it.toDomain(forDate = localDate) }
            }
    }

    override fun getByDateRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Task>>> =
        taskLocalDataSource.getTasksInRange(from, to)
            .map { list ->
                buildMap<LocalDate, MutableList<Task>> {
                    list.forEach { relation ->
                        val due = relation.task.dueDate ?: return@forEach
                        val recurrence = relation.task.toRecurrence()
                        if (recurrence == null) {
                            getOrPut(due) { mutableListOf() }
                                .add(relation.toDomain())
                        } else {
                            var date = maxOf(from, due)
                            val end = recurrence.endDate?.let { minOf(it, to) } ?: to
                            while (date <= end) {
                                if (date.matchesRecurrence(recurrence, due)) {
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
                        val due = relation.task.dueDate ?: return@mapNotNull null
                        val overdueDate = latestOccurrenceBefore(today, due, recurrence)
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
