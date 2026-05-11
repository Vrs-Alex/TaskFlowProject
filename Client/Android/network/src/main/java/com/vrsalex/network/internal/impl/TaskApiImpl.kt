package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import vrsalex.shared.api.item.task.TaskUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid


class TaskApiImpl(
    private val client: HttpClient
): TaskApi {

    override suspend fun markComplete(data: TaskLogCreateRequest): NetworkResult<TaskLogDto> =
        safeCall {
            client.post("task-logs"){
                setBody(data)
            }
        }

    override suspend fun unmarkComplete(
        id: Uuid,
        serverId: Long,
        version: Int
    ): NetworkResult<Unit> =
        safeCall {
            client.delete("task-logs/$id"){
                parameter("version", version)
                parameter("id", serverId)
            }
        }

    override suspend fun getTaskLogs(lastSync: Instant?): NetworkResult<List<ModelDto<TaskLogDto>>> =
        safeCall {
            client.get("task-logs/sync") {
                lastSync?.let { parameter("lastSync", it.toString()) }
            }
        }

    override suspend fun getTaskLog(id: Uuid): NetworkResult<TaskLogDto?> =
        safeCall {
            client.get("task-logs/$id")
        }

    override suspend fun getById(id: Uuid): NetworkResult<TaskDto?> =
        safeCall {
            client.get("tasks/${id}")
        }

    override suspend fun sync(lastSync: Instant?): NetworkResult<List<ModelDto<TaskDto>>> =
        safeCall {
            client.get("tasks/sync") {
                lastSync?.let { parameter("lastSync", it.toString()) }
            }
        }

    override suspend fun syncItem(id: Uuid): NetworkResult<ModelDto<TaskDto>> =
        safeCall {
            client.get("tasks/sync/$id")
        }

    override suspend fun create(data: TaskCreateRequest): NetworkResult<TaskDto> =
        safeCall {
            client.post("tasks"){
                setBody(data)
            }
        }

    override suspend fun update(data: TaskUpdateRequest): NetworkResult<TaskDto> =
        safeCall {
        client.patch("tasks"){
            setBody(data)
        }
    }

    override suspend fun delete(
        id: Uuid,
        serverId: Long,
        version: Int
    ): NetworkResult<Unit> =
        safeCall {
            client.delete("tasks/$id"){
                parameter("version", version)
                parameter("id", serverId)
            }
        }
}