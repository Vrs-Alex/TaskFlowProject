package com.vrsalex.taskflow.data.workspace.tag

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.domain.workspace.tag.TagCreate
import com.vrsalex.taskflow.domain.workspace.tag.TagUpdate
import vrsalex.shared.api.tag.TagDto
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.uuid.Uuid

class TagLocalDataSource(private val db: AppDatabase) {

    private val dao get() = db.tagDao()

    fun observeAll(): Flow<List<TagEntity>> = dao.observeAll()
    fun observe(id: Uuid): Flow<TagEntity?> = dao.observe(id)
    suspend fun getRaw(id: Uuid) = dao.getRaw(id)

    suspend fun create(data: TagCreate) = dao.upsert(data.toEntity())

    suspend fun upsertFromRemote(dto: TagDto) = dao.upsert(dto.toEntity())

    suspend fun update(data: TagUpdate) = db.withTransaction {
        val current = dao.getRaw(data.syncModelUpdate.id) ?: return@withTransaction
        var e = current
        data.name.onDefined { e = e.copy(name = it.value) }
        data.color.onDefined { e = e.copy(color = it.value) }
        dao.upsert(e.copy(sync = e.sync.copy(isSynced = false, updatedAt = Clock.System.now())))
    }

    suspend fun softDelete(id: Uuid) = dao.softDelete(id)
    suspend fun delete(id: Uuid) = dao.delete(id)
}
