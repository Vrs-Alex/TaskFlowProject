package com.vrsalex.taskflow.data.local.db.datasource

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItemTagsAndArea
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventLocalDataSource(
    private val db: AppDatabase,
    private val itemLocalDataSource: ItemLocalDataSource
) {

    fun getEvents(): Flow<List<EventWithItemTagsAndArea>> =
        db.eventDao().getEvents()

    fun getEvents(date: Instant): Flow<List<EventWithItemTagsAndArea>> =
        db.eventDao().getEvents(date)


    suspend fun insert(item: ItemEntity, event: EventEntity, tags: List<Uuid>) {
        db.withTransaction {
            itemLocalDataSource.insert(item, tags)
            db.eventDao().upsert(event)
        }
    }

    suspend fun update(item: ItemEntity, event: EventEntity) {
        db.withTransaction {
            db.itemDao().update(item)
            db.eventDao().update(event)
        }
    }

    suspend fun delete(id: Uuid) {
        db.itemDao().delete(id)
    }


}