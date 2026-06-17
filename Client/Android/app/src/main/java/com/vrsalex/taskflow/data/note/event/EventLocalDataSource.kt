package com.vrsalex.taskflow.data.note.event

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import com.vrsalex.taskflow.data.note.applyTo
import com.vrsalex.taskflow.data.note.tagIds
import com.vrsalex.taskflow.data.note.tagIdsOrNull
import com.vrsalex.taskflow.data.note.toEntity
import com.vrsalex.taskflow.domain.note.event.EventCreate
import com.vrsalex.taskflow.domain.note.event.EventUpdate
import vrsalex.shared.api.item.event.EventDto
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventLocalDataSource(private val db: AppDatabase) {

    private val noteDao get() = db.noteDao()
    private val eventDao get() = db.eventDao()

    fun observeAll(): Flow<List<EventRelation>> = eventDao.observeAll()
    fun observe(id: Uuid): Flow<EventRelation?> = eventDao.observe(id)
    fun observeBetween(from: Instant, to: Instant): Flow<List<EventRelation>> = eventDao.observeBetween(from, to)
    fun observeArchived(query: String): Flow<List<EventRelation>> = eventDao.observeArchived(query)
    suspend fun getRaw(id: Uuid) = eventDao.getRaw(id)

    suspend fun create(data: EventCreate) = db.withTransaction {
        noteDao.upsertWithTags(data.note.toEntity(), data.note.tagIds())
        eventDao.upsert(data.toEventEntity())
    }

    suspend fun upsertFromRemote(dto: EventDto) = db.withTransaction {
        noteDao.upsertWithTags(dto.base.toEntity(), dto.base.tags)
        eventDao.upsert(dto.toEventEntity())
    }

    suspend fun update(data: EventUpdate) = db.withTransaction {
        val currentNote = noteDao.getRaw(data.note.syncModelUpdate.id) ?: return@withTransaction
        val updatedNote = data.note.applyTo(currentNote)
        val tagIds = data.note.tagIdsOrNull()
        if (tagIds != null) noteDao.upsertWithTags(updatedNote, tagIds) else noteDao.upsert(updatedNote)

        val currentEvent = eventDao.getRaw(currentNote.id)?.event ?: return@withTransaction
        var e = currentEvent
        data.startDate.onDefined { e = e.copy(startDate = it) }
        data.endDate.onDefined { e = e.copy(endDate = it) }
        data.isAllDay.onDefined { e = e.copy(isAllDay = it) }
        data.location.onDefined { e = e.copy(location = it) }
        eventDao.upsert(e)
    }

    suspend fun softDelete(id: Uuid) = eventDao.softDelete(id)
    suspend fun delete(id: Uuid) = eventDao.delete(id)
}
