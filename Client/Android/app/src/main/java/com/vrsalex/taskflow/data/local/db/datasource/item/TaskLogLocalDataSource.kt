package com.vrsalex.taskflow.data.local.db.datasource.item

import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.datasource.sync.SyncLocalDataSource
import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskLogLocalDataSource(private val db: AppDatabase) : SyncLocalDataSource {

    suspend fun getByIdRaw(id: Uuid): TaskLogEntity? =
        db.taskLogDao().getByIdRaw(id)

    fun geByTaskId(taskId: Uuid): Flow<List<TaskLogEntity>> =
        db.taskLogDao().getByTaskId(taskId)

    suspend fun getByTaskAndDate(taskClientId: Uuid, date: LocalDate): TaskLogEntity? =
        db.taskLogDao().getByTaskAndDate(taskClientId, date)

    suspend fun insert(taskLog: TaskLogEntity) =
        db.taskLogDao().upsert(taskLog)

    suspend fun softDelete(id: Uuid) =
        db.taskLogDao().softDelete(id)

    suspend fun delete(id: Uuid) =
        db.taskLogDao().delete(id)

    suspend fun undelete(id: Uuid) = db.taskLogDao().undelete(id)

    override suspend fun markSynced(id: Uuid, newId: Uuid, serverId: Long, version: Int, updatedAt: Instant) =
        db.taskLogDao().markSynced(id, newId, serverId, version, updatedAt)

}
