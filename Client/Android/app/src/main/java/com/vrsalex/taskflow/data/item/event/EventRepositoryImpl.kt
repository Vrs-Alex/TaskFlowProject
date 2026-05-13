package com.vrsalex.taskflow.data.item.event

import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.taskflow.data.item.base.BaseItemRepositoryImpl
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.local.db.datasource.item.EventLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vrsalex.shared.api.item.event.EventDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventRepositoryImpl(
    private val eventApi: EventApi,
    private val eventLocalDataSource: EventLocalDataSource,
    itemLocalDataSource: ItemLocalDataSource,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : BaseItemRepositoryImpl<EventDto, EventCreate, EventUpdate, Event>(
    syncEntity = SyncDbEntity.EVENT,
    api = eventApi,
    itemLocalDataSource = itemLocalDataSource,
    outboxHandler = outboxHandler,
    syncHandler = syncHandler,
), EventRepository {

    override suspend fun outboxCreate(id: Uuid): Resource<SyncModel> {
        val event = eventLocalDataSource.getEventByIdRaw(id)
            ?: return Resource.Failure.Error("Event not found")
        return eventApi.create(event.toDomain().toCreateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun outboxUpdate(id: Uuid): Resource<SyncModel> {
        val event = eventLocalDataSource.getEventByIdRaw(id)
            ?: return Resource.Failure.Error("Event not found")
        return eventApi.update(event.toDomain().toUpdateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun insert(dto: EventDto) {
        eventLocalDataSource.insert(
            item = dto.base.toEntityWithRelations(),
            event = dto.toEntity()
        )
    }

    override suspend fun localInsert(data: EventCreate): Uuid {
        eventLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            event = data.toEntity()
        )
        return data.base.id
    }

    override suspend fun localUpdate(data: EventUpdate): Uuid {
        eventLocalDataSource.update(data)
        return data.base.id
    }

    override fun observeAll(): Flow<List<Event>> =
        eventLocalDataSource.getEvents().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Event?> =
        eventLocalDataSource.getEvent(id).map { it?.toDomain() }

    override fun getArchived(query: String): Flow<List<Event>> =
        eventLocalDataSource.getArchivedEvents(query).map { list -> list.map { it.toDomain() } }

    override fun getByDate(date: Instant): Flow<List<Event>> =
        eventLocalDataSource.getEvents(date).map { list -> list.map { it.toDomain() } }
}
