package com.vrsalex.taskflow.data.item.task

import com.vrsalex.network.public.api.item.TaskApi
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
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskRepositoryImpl(
    private val taskApi: TaskApi,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val itemLocalDataSource: ItemLocalDataSource,
    private val taskLogRepository: TaskLogRepository,
    private val syncHandler: SyncHandler,
    private val outboxHandler: OutboxHandler
) : TaskRepository {

    init {
        outboxHandler.register(
            SyncDbEntity.TASK,
            object : OutboxEntityHandler {

                override suspend fun create(id: Uuid): Resource<SyncModel> {
                    val task = taskLocalDataSource.getTaskByIdRaw(id)
                        ?: return Resource.Error("Task not found")
                    return taskApi.create(task.toDomain().toCreateDto())
                        .toResource { it.toSyncModel() }
                }

                override suspend fun update(id: Uuid): Resource<SyncModel> {
                    val task = taskLocalDataSource.getTaskByIdRaw(id)
                        ?: return Resource.Error("Task not found")
                    return taskApi.update(task.toDomain().toUpdateDto())
                        .toResource { it.toSyncModel() }
                }

                override suspend fun delete(id: Uuid): Resource<Unit> {
                    val item = itemLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Error("Item not found")
                    val serverId = item.serverId ?: return Resource.Error("ServerId not found")
                    return taskApi.delete(item.id, serverId, item.version)
                        .toResource { itemLocalDataSource.delete(id) }
                }

                override suspend fun markAsSynced(id: Uuid, syncModel: SyncModel) {
                    itemLocalDataSource.markSynced(
                        id = id,
                        newId = syncModel.id,
                        serverId = syncModel.serverId ?: return,
                        version = syncModel.version,
                        updatedAt = syncModel.updatedAt
                    )
                }

                override suspend fun findExisting(itemId: Uuid): SyncModel? {
                    val result = taskApi.getById(itemId).toResource { it?.toSyncModel() }
                    return (result as? Resource.Success)?.data
                }
            }
        )
    }

    override fun get(): Flow<List<Task>> =
        taskLocalDataSource.getTasks().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Task?> =
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

    override suspend fun create(data: TaskCreate) {
        taskLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            task = data.toEntity()
        )
        outboxHandler.addOperation(data.base.id, SyncDbEntity.TASK, PendingOperation.CREATE)
    }

    override suspend fun update(data: TaskUpdate) {
        taskLocalDataSource.update(data)
        outboxHandler.addOperation(data.base.id, SyncDbEntity.TASK, PendingOperation.UPDATE)
    }

    override suspend fun delete(id: Uuid) {
        val item = itemLocalDataSource.getByIdRaw(id)
        if (item?.serverId == null) {
            itemLocalDataSource.delete(id)
            return
        }
        itemLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, SyncDbEntity.TASK, PendingOperation.DELETE)
    }

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncHandler.sync(
            syncEntity = SyncDbEntity.TASK,
            lastSync = lastSync,
            fetch = taskApi::sync,
            insert = { data ->
                taskLocalDataSource.insert(
                    item = data.base.toEntityWithRelations(),
                    task = data.toEntity()
                )
            },
            delete = itemLocalDataSource::delete,
            getLocalSyncableModel = { dto -> itemLocalDataSource.getByIdRaw(dto.clientId) }
        )

    override suspend fun syncItem(id: Uuid): Resource<Unit> =
        syncHandler.syncItem(
            id = id,
            fetchItem = taskApi::syncItem,
            insert = { data ->
                taskLocalDataSource.insert(
                    item = data.base.toEntityWithRelations(),
                    task = data.toEntity()
                )
            },
            delete = itemLocalDataSource::delete
        )


    override suspend fun changeMarkAsDone(data: TaskLogCreate, isDone: Boolean) {
        if (isDone) {
            taskLogRepository.markAsDone(data)
        } else {
            taskLogRepository.markAsUndone(data.id)
        }
    }
}
