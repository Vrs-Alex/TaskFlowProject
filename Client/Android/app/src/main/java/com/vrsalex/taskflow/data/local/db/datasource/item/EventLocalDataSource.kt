package com.vrsalex.taskflow.data.local.db.datasource.item

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.item.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import com.vrsalex.taskflow.data.local.db.relation.ItemWithRelations
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventLocalDataSource(
    private val db: AppDatabase,
    private val itemLocalDataSource: ItemLocalDataSource
) {

    suspend fun getEventByIdRaw(id: Uuid): EventRelation? =
        db.eventDao().getEventByIdRaw(id)

    fun getEvent(id: Uuid): Flow<EventRelation?> =
        db.eventDao().getEvent(id)

    fun getEvents(): Flow<List<EventRelation>> =
        db.eventDao().getEvents()

    fun getArchivedEvents(query: String = ""): Flow<List<EventRelation>> =
        db.eventDao().getArchivedEvents(query)


    fun getEvents(date: Instant): Flow<List<EventRelation>> {
        val tz = TimeZone.currentSystemDefault()
        val localDate = date.toLocalDateTime(tz).date
        val startOfDay = localDate.atStartOfDayIn(tz)
        val endOfDay = localDate.plus(1, DateTimeUnit.DAY).atStartOfDayIn(tz)
        return db.eventDao().getEvents(startOfDay, endOfDay)
    }

    suspend fun insert(item: ItemWithRelations, event: EventEntity) {
        db.withTransaction {
            itemLocalDataSource.insert(item)
            db.eventDao().upsert(event)
        }
    }

    suspend fun update(data: EventUpdate) {
        db.withTransaction {
            val current = db.eventDao().getEventByIdRaw(data.base.id)
                ?: return@withTransaction

            itemLocalDataSource.update(data.base)

            var updatedEvent = current.event
            data.startDate.onDefined { updatedEvent = updatedEvent.copy(startDate = it) }
            data.endDate.onDefined { updatedEvent = updatedEvent.copy(endDate = it) }
            data.isAllDay.onDefined { updatedEvent = updatedEvent.copy(isAllDay = it) }
            data.location.onDefined { updatedEvent = updatedEvent.copy(location = it) }

            db.eventDao().update(updatedEvent)
        }
    }
}