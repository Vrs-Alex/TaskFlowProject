package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.item.EventApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal class EventApiImpl(
    private val client: HttpClient
): EventApi {

    override suspend fun getById(id: Uuid): NetworkResult<EventDto?> =
        safeCall {
            client.get("events/${id}")
        }


    override suspend fun sync(lastSync: Instant?): NetworkResult<List<ModelDto<EventDto>>> =
        safeCall {
            client.get("events/sync"){
                lastSync?.let {
                    parameter("lastSync", it.toString())
                }
            }
        }

    override suspend fun syncItem(id: Uuid): NetworkResult<ModelDto<EventDto>> =
        safeCall {
            client.get("events/sync/$id")
        }

    override suspend fun create(data: EventCreateRequest): NetworkResult<EventDto> =
        safeCall {
            client.post("events"){
                setBody(data)
            }
        }

    override suspend fun update(data: EventUpdateRequest): NetworkResult<EventDto> =
        safeCall {
            client.patch ("events"){
                setBody(data)
            }
        }

    override suspend fun delete(id: Uuid, serverId: Long, version: Int): NetworkResult<Unit> =
        safeCall {
            client.delete("events/$id"){
                parameter("version", version)
                parameter("id", serverId)
            }
        }
}