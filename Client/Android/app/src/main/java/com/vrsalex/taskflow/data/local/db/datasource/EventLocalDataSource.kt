package com.vrsalex.taskflow.data.local.db.datasource

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItem
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventLocalDataSource(
    private val db: AppDatabase
) {

    fun getEvents(): Flow<List<EventWithItem>> =
        db.eventDao().getEventsWithItems()

    fun getEvents(date: Instant): Flow<List<EventWithItem>> =
        db.eventDao().getEventsWithItems(date)


    suspend fun insert(item: ItemEntity, event: EventEntity) {
        db.withTransaction {
            db.itemDao().insert(item)
            db.eventDao().insert(event)
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