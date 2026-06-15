package com.vrsalex.taskflow.data.item.event

import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.taskflow.data.item.base.ItemSyncEngine
import com.vrsalex.taskflow.data.item.base.RemoteSync
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.local.db.datasource.item.EventLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventRepositoryImpl(
    eventApi: EventApi,
    private val eventLocalDataSource: EventLocalDataSource,
    itemLocalDataSource: ItemLocalDataSource,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : EventRepository {

    private val engine = ItemSyncEngine(
        syncEntity = SyncDbEntity.EVENT,
        api = eventApi,
        itemLocalDataSource = itemLocalDataSource,
        outboxHandler = outboxHandler,
        syncHandler = syncHandler,
        remote = object : RemoteSync<EventDto, EventCreateRequest, EventUpdateRequest> {
            override suspend fun applyRemote(dto: EventDto) {
                eventLocalDataSource.insert(
                    item = dto.base.toEntityWithRelations(),
                    event = dto.toEntity()
                )
            }

            override suspend fun buildCreate(id: Uuid): EventCreateRequest? =
                eventLocalDataSource.getEventByIdRaw(id)?.toDomain()?.toCreateDto()

            override suspend fun buildUpdate(id: Uuid): EventUpdateRequest? =
                eventLocalDataSource.getEventByIdRaw(id)?.toDomain()?.toUpdateDto()
        },
    )

    override fun get(): Flow<List<Event>> =
        eventLocalDataSource.getEvents().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Event?> =
        eventLocalDataSource.getEvent(id).map { it?.toDomain() }

    override fun getByDate(date: Instant): Flow<List<Event>> =
        eventLocalDataSource.getEvents(date).map { list -> list.map { it.toDomain() } }

    override fun getArchived(query: String): Flow<List<Event>> =
        eventLocalDataSource.getArchivedEvents(query).map { list -> list.map { it.toDomain() } }

    override suspend fun create(data: EventCreate) {
        eventLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            event = data.toEntity()
        )
        engine.enqueueCreate(data.base.id)
    }

    override suspend fun update(data: EventUpdate) {
        eventLocalDataSource.update(data)
        engine.enqueueUpdate(data.base.id)
    }

    override suspend fun delete(id: Uuid) = engine.delete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = engine.sync(lastSync)

    override suspend fun syncItem(id: Uuid): Resource<Unit> = engine.syncItem(id)
}
