package com.vrsalex.taskflow.data.local.db.datasource

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.relation.ItemWithRelations
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import kotlin.time.Instant
import kotlin.uuid.Uuid


class ItemLocalDataSource(private val db: AppDatabase): SyncLocalDataSource {

    fun getById(id: Uuid) = db.itemDao().getItem(id)

    suspend fun getByIdRaw(id: Uuid): ItemEntity? = db.itemDao().getByIdRaw(id)

    suspend fun insert(item: ItemWithRelations) {
        db.withTransaction {
            db.itemDao().upsert(item.entity)
            db.itemTagDao().deleteByItemId(item.entity.id)
            item.tags.forEach { tagId ->
                val tagPk = db.tagDao().getById(tagId)?.id ?: return@forEach
                db.itemTagDao().insert(ItemTagCrossRef(item.entity.id, tagPk))
            }
        }
    }

    suspend fun update(data: ItemUpdate) {
        db.withTransaction {
            val current = db.itemDao().getByIdRaw(data.id) ?: return@withTransaction

            var updated = current.copy(isSynced = false)
            data.name.onDefined { updated = updated.copy(name = it) }
            data.description.onDefined { updated = updated.copy(description = it) }
            data.status.onDefined { updated = updated.copy(status = it) }
            data.priority.onDefined { updated = updated.copy(priority = it) }
            data.areaId.onDefined { updated = updated.copy(areaId = it) }

            db.itemDao().update(updated)

            data.tagIds.onDefined { tagIds ->
                db.itemTagDao().deleteByItemId(data.id)
                tagIds.forEach { tagClientId ->
                    val tagPk = db.tagDao().getById(tagClientId)?.id ?: return@forEach
                    db.itemTagDao().insert(ItemTagCrossRef(data.id, tagPk))
                }
            }
        }
    }

    override suspend fun markSynced(id: Uuid, newId: Uuid, serverId: Long, version: Int, updatedAt: Instant) =
        db.itemDao().markSynced(id, newId, serverId, version, updatedAt)

    suspend fun delete(id: Uuid) = db.itemDao().delete(id)

    suspend fun softDelete(id: Uuid) = db.itemDao().softDelete(id)
}