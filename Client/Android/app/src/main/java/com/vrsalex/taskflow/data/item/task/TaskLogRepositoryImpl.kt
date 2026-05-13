package com.vrsalex.taskflow.data.item.task

import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLogLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.item.task.TaskLogCreate
import com.vrsalex.taskflow.domain.item.task.TaskLogRepository
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskLogRepositoryImpl(
    private val taskApi: TaskApi,
    private val taskLogLocalDataSource: TaskLogLocalDataSource,
    private val itemLocalDataSource: ItemLocalDataSource,
    private val syncHandler: SyncHandler,
    private val outboxHandler: OutboxHandler
) : TaskLogRepository {

    init {
        outboxHandler.register(
            SyncDbEntity.TASK_LOG,
            object : OutboxEntityHandler {

                override suspend fun create(id: Uuid): Resource<SyncModel> {
                    val log = taskLogLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Failure.Error("TaskLog not found")
                    val taskServerId = itemLocalDataSource.getByIdRaw(log.taskId)?.serverId
                    return taskApi.markComplete(log.toDomain().let {
                        TaskLogCreate(id = it.id, taskId = it.taskId, date = it.date)
                            .toDto(taskServerId)
                    }).toResource { it.toSyncModel() }
                }

                override suspend fun update(id: Uuid): Resource<SyncModel> =
                    Resource.Failure.Error("TaskLog does not support update")

                override suspend fun delete(id: Uuid): Resource<Unit> {
                    val log = taskLogLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Failure.Error("TaskLog not found")
                    val serverId = log.serverId ?: return Resource.Failure.Error("ServerId not found")
                    return taskApi.unmarkComplete(log.id, serverId, log.version)
                        .toResource { taskLogLocalDataSource.delete(id) }
                }

                override suspend fun markAsSynced(id: Uuid, syncModel: SyncModel) {
                    taskLogLocalDataSource.markSynced(
                        id = id,
                        newId = syncModel.id,
                        serverId = syncModel.serverId ?: return,
                        version = syncModel.version,
                        updatedAt = syncModel.updatedAt
                    )
                }

                override suspend fun findExisting(itemId: Uuid): SyncModel? {
                    val result = taskApi.getTaskLog(itemId).toResource { it?.toSyncModel() }
                    return (result as? Resource.Success)?.data
                }
            }
        )
    }

    override suspend fun markAsDone(data: TaskLogCreate) {
        val existing = taskLogLocalDataSource.getByTaskAndDate(data.taskId, data.date)
        when {
            existing == null -> {
                taskLogLocalDataSource.insert(data.toEntity())
                outboxHandler.addOperation(data.id, SyncDbEntity.TASK_LOG, PendingOperation.CREATE)
            }
            existing.isDeleted -> {
                taskLogLocalDataSource.undelete(existing.id)
                outboxHandler.addOperation(existing.id, SyncDbEntity.TASK_LOG, PendingOperation.CREATE)
            }
        }
    }

    override suspend fun markAsUndone(id: Uuid) {
        val log = taskLogLocalDataSource.getByIdRaw(id) ?: return
        if (log.serverId == null) {
            taskLogLocalDataSource.delete(id)
            outboxHandler.cancelOperation(id)
            return
        }
        taskLogLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, SyncDbEntity.TASK_LOG, PendingOperation.DELETE)
    }

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncHandler.sync(
            syncEntity = SyncDbEntity.TASK_LOG,
            lastSync = lastSync,
            fetch = taskApi::getTaskLogs,
            insert = { dto -> taskLogLocalDataSource.insert(dto.toEntity()) },
            delete = { id -> taskLogLocalDataSource.delete(id) },
            getLocalSyncableModel = { dto -> taskLogLocalDataSource.getByIdRaw(dto.clientId) }
        )
}
