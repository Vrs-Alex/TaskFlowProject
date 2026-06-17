package com.vrsalex.taskflow.domain.sync.service

import android.util.Log
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.task.TaskLogRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.workspace.area.AreaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.time.Instant

class SyncService(
    private val areaRepository: AreaRepository,
    private val tagRepository: AreaRepository,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
    private val taskLogRepository: TaskLogRepository,
    private val eventRepository: EventRepository
) {

    suspend fun syncAll(): List<Resource<Unit>> = coroutineScope {
//        outboxHandler.process()
        listOf(
            async { areaRepository.sync(null) },
            async { tagRepository.sync(null) },
            async { noteRepository.sync(null) },
            async { taskRepository.sync(null) },
            async { taskLogRepository.sync(null) },
            async { eventRepository.sync(null) },
        ).awaitAll()
    }

    suspend fun syncEntity(entity: SyncEntity, time: Instant) = coroutineScope {
//        outboxHandler.process()
        when (entity) {
            SyncEntity.AREA -> areaRepository.sync(time)
            SyncEntity.TAG -> tagRepository.sync(time)
            SyncEntity.NOTE -> noteRepository.sync(time)
            SyncEntity.TASK -> taskRepository.sync(time)
            SyncEntity.TASK_LOG -> taskLogRepository.sync(time)
            SyncEntity.EVENT -> eventRepository.sync(time)
        }
    }

}