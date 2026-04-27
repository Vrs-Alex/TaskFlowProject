package com.vrsalex.taskflow.data.item.event

import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.taskflow.data.item.base.toEntity
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.local.db.datasource.EventLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.ItemLocalDataSource
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventRepositoryImpl(
    private val eventApi: EventApi,
    private val eventLocalDataSource: EventLocalDataSource,
    private val itemLocalDataSource: ItemLocalDataSource,
    private val syncHandler: SyncHandler,
    private val outboxHandler: OutboxHandler
) : EventRepository {

    init {
        outboxHandler.register(SyncDbEntity.EVENT, object : OutboxEntityHandler {
            override suspend fun create(id: Uuid): Resource<Unit> {
                val event = eventLocalDataSource.getEventByIdRaw(id)
                    ?: return Resource.Error("Event not found")
                return eventApi.create(event.toDomain().toCreateDto()).toResource { Unit }
            }

            override suspend fun update(id: Uuid): Resource<Unit> {
                val event = eventLocalDataSource.getEventByIdRaw(id)
                    ?: return Resource.Error("Event not found")
                return eventApi.update(event.toDomain().toUpdateDto()).toResource { Unit }
            }

            override suspend fun delete(id: Uuid): Resource<Unit> {
                val item = itemLocalDataSource.getByIdRaw(id)
                    ?: return Resource.Error("Item not found")
                val serverId = item.serverId
                    ?: return Resource.Error("ServerId not found")
                return eventApi.delete(item.id, serverId, item.version).toResource { Unit }
            }
        })
    }

    override fun get(): Flow<List<Event>> =
        eventLocalDataSource.getEvents().map { list -> list.map { it.toDomain() } }

    override fun getByDate(date: Instant): Flow<List<Event>> =
        eventLocalDataSource.getEvents(date).map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Event?> =
        eventLocalDataSource.getEvent(id).map { it?.toDomain() }

    override suspend fun create(data: EventCreate) {
        eventLocalDataSource.insert(
            item = data.base.toEntityWithRelations(),
            event = data.toEntity()
        )
        outboxHandler.addOperation(data.base.id, SyncDbEntity.EVENT, PendingOperation.CREATE)
    }

    override suspend fun update(data: EventUpdate) {
        eventLocalDataSource.update(data)
        outboxHandler.addOperation(data.base.id, SyncDbEntity.EVENT, PendingOperation.UPDATE)
    }

    override suspend fun delete(id: Uuid) {
        val item = itemLocalDataSource.getByIdRaw(id)
        if (item?.serverId == null) {
            itemLocalDataSource.delete(id)
            return
        }
        itemLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, SyncDbEntity.EVENT, PendingOperation.DELETE)
    }

    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.EVENT,
            lastSync = lastSync,
            fetch = eventApi::get,
            insert = { data ->
                eventLocalDataSource.insert(
                    item = data.base.toEntityWithRelations(),
                    event = data.toEntity()
                )
            },
            delete = itemLocalDataSource::delete,
            getLocalItem = { dto ->
                itemLocalDataSource.getByIdRaw(dto.clientId)
            }
        )
}