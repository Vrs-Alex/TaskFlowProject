package com.vrsalex.taskflow.domain.sync

import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.time.Instant

class SyncUseCase(
    private val eventRepository: EventRepository,
    private val areaRepository: AreaRepository,
    private val tagRepository: TagRepository,
    private val outboxHandler: OutboxHandler
) {

    suspend fun syncAll(): List<Resource<Unit>> = coroutineScope {
        outboxHandler.process()

        listOf(
            async { eventRepository.sync(null) },
            async { areaRepository.sync(null) },
            async { tagRepository.sync(null) },
        ).awaitAll()
    }

    suspend fun syncEntity(entity: SyncDbEntity, time: Instant) = coroutineScope {
        outboxHandler.process()
        when (entity) {
            SyncDbEntity.EVENT -> eventRepository.sync(time)
            SyncDbEntity.AREA -> areaRepository.sync(time)
            SyncDbEntity.TAG -> tagRepository.sync(time)
            SyncDbEntity.TASK -> TODO()
            SyncDbEntity.GOAL -> TODO()
            SyncDbEntity.HABIT -> TODO()
            SyncDbEntity.REMINDER -> TODO()
            SyncDbEntity.ATTACHMENT -> TODO()
            SyncDbEntity.RECURRENCE -> TODO()
        }
    }
}