package com.vrsalex.taskflow.domain.sync

import com.vrsalex.taskflow.domain.area.AreaRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.time.Instant

class SyncUseCase(
    private val eventRepository: EventRepository,
    private val areaRepository: AreaRepository,
) {

    suspend fun syncAll(): List<Resource<Unit>> = coroutineScope {
        listOf(
            async { eventRepository.sync(null) },
            async { areaRepository.sync(null) },
        ).awaitAll()
    }

    suspend fun syncEntity(entity: SyncDbEntity, time: Instant) = when (entity) {
        SyncDbEntity.EVENT -> eventRepository.sync(time)
        SyncDbEntity.AREA  -> areaRepository.sync(time)
        SyncDbEntity.TAG -> TODO()
        SyncDbEntity.TASK -> TODO()
        SyncDbEntity.GOAL -> TODO()
        SyncDbEntity.HABIT -> TODO()
        SyncDbEntity.REMINDER -> TODO()
        SyncDbEntity.ATTACHMENT -> TODO()
        SyncDbEntity.RECURRENCE -> TODO()
    }
}