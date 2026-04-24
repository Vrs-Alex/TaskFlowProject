package com.vrsalex.taskflow.data.local.db.datasource

import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import kotlin.uuid.Uuid

class ItemLocalDataSource(private val db: AppDatabase) {

    suspend fun insert(
        item: ItemEntity,
        tags: List<Uuid> = emptyList()
    ) {
        db.itemDao().upsert(item)
        db.itemTagDao().deleteByItemId(item.id)
        tags.forEach { tagClientId ->
            val tagId = db.tagDao().getById(tagClientId)?.id ?: return@forEach
            db.itemTagDao().insert(ItemTagCrossRef(item.id, tagId))
        }
    }

    suspend fun delete(id: Uuid) = db.itemDao().delete(id)
}