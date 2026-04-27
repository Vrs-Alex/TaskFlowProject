package com.vrsalex.taskflow.data.local.db.datasource

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.domain.workscape.tag.TagUpdate
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TagLocalDataSource(private val db: AppDatabase) {

    fun getTags(): Flow<List<TagEntity>> = db.tagDao().getTags()
    
    fun getTag(id: Uuid): Flow<TagEntity?> = db.tagDao().getTag(id)

    suspend fun getByIdRaw(id: Uuid): TagEntity? = db.tagDao().getByIdRaw(id)

    suspend fun insert(tag: TagEntity) = db.tagDao().upsert(tag)

    suspend fun update(data: TagUpdate) {
        db.withTransaction {
            val current = db.tagDao().getByIdRaw(data.id) ?: return@withTransaction
            
            var updated = current.copy(isSynced = false)
            data.name.onDefined { updated = updated.copy(name = it) }
            data.color.onDefined { updated = updated.copy(color = it) }
            
            db.tagDao().update(updated)
        }
    }

    suspend fun markSynced(id: Uuid, serverId: Long, version: Int, updatedAt: Instant) =
        db.tagDao().markSynced(id, serverId, version, updatedAt)

    suspend fun delete(id: Uuid) = db.tagDao().delete(id)
    
    suspend fun softDelete(id: Uuid) = db.tagDao().softDelete(id)
}