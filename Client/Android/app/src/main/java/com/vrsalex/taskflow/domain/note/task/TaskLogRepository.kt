package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface TaskLogRepository {

    suspend fun markAsDone(data: TaskLogCreate)

    suspend fun markAsUndone(id: Uuid)

    suspend fun sync(lastSync: Instant?): Resource<Unit>

    suspend fun push(): Resource<Unit>

}