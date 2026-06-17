package com.vrsalex.taskflow.data.workspace.area

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.domain.workspace.area.AreaCreate
import com.vrsalex.taskflow.domain.workspace.area.AreaUpdate
import kotlinx.coroutines.flow.Flow
import vrsalex.shared.api.area.AreaDto
import kotlin.time.Clock
import kotlin.uuid.Uuid

class AreaLocalDataSource(private val db: AppDatabase) {

    private val dao get() = db.areaDao()

    fun observeAll(): Flow<List<AreaEntity>> = dao.observeAll()
    fun observe(id: Uuid): Flow<AreaEntity?> = dao.observe(id)
    suspend fun getRaw(id: Uuid) = dao.getRaw(id)

    suspend fun create(data: AreaCreate) = dao.upsert(data.toEntity())
    suspend fun upsertFromRemote(dto: AreaDto) = dao.upsert(dto.toEntity())

    suspend fun update(data: AreaUpdate) = db.withTransaction {
        val current = dao.getRaw(data.syncModelUpdate.id) ?: return@withTransaction
        var e = current
        data.name.onDefined { e = e.copy(name = it.value) }
        data.color.onDefined { e = e.copy(color = it.value) }
        dao.upsert(e.copy(sync = e.sync.copy(isSynced = false, updatedAt = Clock.System.now())))
    }

    suspend fun softDelete(id: Uuid) = dao.softDelete(id)
    suspend fun delete(id: Uuid) = dao.delete(id)
}
