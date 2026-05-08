package com.vrsalex.network.public.api.item

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.network.public.common.SyncApi
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import vrsalex.shared.api.item.task.TaskUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface TaskApi : SyncApi<TaskDto, TaskCreateRequest, TaskUpdateRequest> {

    suspend fun markComplete(data: TaskLogCreateRequest): NetworkResult<TaskLogDto>

    suspend fun unmarkComplete(id: Uuid, serverId: Long, version: Int): NetworkResult<Unit>

    suspend fun getTaskLogs(lastSync: Instant?): NetworkResult<List<ModelDto<TaskLogDto>>>

}