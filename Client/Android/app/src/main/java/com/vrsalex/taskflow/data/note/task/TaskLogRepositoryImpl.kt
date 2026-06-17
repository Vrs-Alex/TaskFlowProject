package com.vrsalex.taskflow.data.note.task

import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.task.TaskLogCreate
import com.vrsalex.taskflow.domain.note.task.TaskLogRepository
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskLogRepositoryImpl(
    private val taskApi: TaskApi,
    private val taskLogLocalDataSource: TaskLogLocalDataSource,
    private val syncPuller: SyncPuller
) : TaskLogRepository {

    override suspend fun markAsDone(data: TaskLogCreate) {
//        val existing = taskLogLocalDataSource.getByTaskAndDate(data.taskId, data.date)
//        when {
//            existing == null -> {
//                taskLogLocalDataSource.create(data)
//            }
//            existing.isDeleted -> {
//                taskLogLocalDataSource.undelete(existing.id)
//            }
//        }
    }

    override suspend fun markAsUndone(id: Uuid) {
        val log = taskLogLocalDataSource.getRaw(id) ?: return
        if (log.sync.serverId == null) {
            taskLogLocalDataSource.delete(id)
            return
        }
        taskLogLocalDataSource.softDelete(id)
    }

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncPuller.sync(
            syncEntity = SyncEntity.TASK_LOG,
            lastSync = lastSync,
            fetch = taskApi::getTaskLogs,
            upsert = { dto -> taskLogLocalDataSource.upsertFromRemote(dto) },
            delete = { id -> taskLogLocalDataSource.delete(id) },
            getLocalSyncModelColumns = { id -> taskLogLocalDataSource.getRaw(id) }
        )
}
