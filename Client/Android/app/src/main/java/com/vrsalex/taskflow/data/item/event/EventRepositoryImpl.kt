package com.vrsalex.taskflow.data.item.event

import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.taskflow.data.item.base.toEntity
import com.vrsalex.taskflow.data.local.db.datasource.EventLocalDataSource
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant

class EventRepositoryImpl(
    private val eventApi: EventApi,
    private val eventLocalDataSource: EventLocalDataSource,
    private val syncHandler: SyncHandler
): EventRepository {

    override fun get(): Flow<List<Event>> =
        eventLocalDataSource.getEvents().map { list -> list.map { it.toDomain() } }

    override fun getByDate(date: Instant): Flow<List<Event>> =
        eventLocalDataSource.getEvents(date).map { list -> list.map { it.toDomain() } }


    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.EVENT,
            lastSync = lastSync,
            fetch = eventApi::get,
            insert = { data ->
                eventLocalDataSource.insert(
                    item = data.base.toEntity(),
                    event = data.toEntity()
                )
            },
            delete = eventLocalDataSource::delete
        )

}