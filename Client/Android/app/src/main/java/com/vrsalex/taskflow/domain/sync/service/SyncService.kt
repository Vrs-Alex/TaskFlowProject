package com.vrsalex.taskflow.domain.sync.service

import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.task.TaskLogRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.workspace.area.AreaRepository
import com.vrsalex.taskflow.domain.workspace.tag.TagRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.time.Instant

class SyncService(
    private val areaRepository: AreaRepository,
    private val tagRepository: TagRepository,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
    private val taskLogRepository: TaskLogRepository,
    private val eventRepository: EventRepository
) {

    suspend fun syncAll(): List<Resource<Unit>> = pushAll() + pullAll()

    private suspend fun pushAll(): List<Resource<Unit>> = buildList {
        addAll(coroutineScope {
            listOf(
                async { areaRepository.push() },
                async { tagRepository.push() },
            ).awaitAll()
        })
        addAll(coroutineScope {
            listOf(
                async { noteRepository.push() },
                async { taskRepository.push() },
                async { eventRepository.push() },
            ).awaitAll()
        })
        add(taskLogRepository.push())
    }

    private suspend fun pullAll(): List<Resource<Unit>> = coroutineScope {
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
