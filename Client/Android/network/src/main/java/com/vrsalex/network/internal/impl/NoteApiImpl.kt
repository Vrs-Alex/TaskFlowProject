package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.item.NoteApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal class NoteApiImpl(
    private val client: HttpClient
) : NoteApi {

    override suspend fun getById(id: Uuid): NetworkResult<ItemDto?> =
        safeCall { client.get("notes/$id") }

    override suspend fun sync(lastSync: Instant?): NetworkResult<List<ModelDto<ItemDto>>> =
        safeCall {
            client.get("notes/sync") {
                lastSync?.let { parameter("lastSync", it.toString()) }
            }
        }

    override suspend fun syncItem(id: Uuid): NetworkResult<ModelDto<ItemDto>> =
        safeCall { client.get("notes/sync/$id") }

    override suspend fun create(data: ItemCreateRequest): NetworkResult<ItemDto> =
        safeCall { client.post("notes") { setBody(data) } }

    override suspend fun update(data: ItemUpdateRequest): NetworkResult<ItemDto> =
        safeCall { client.patch("notes") { setBody(data) } }

    override suspend fun delete(id: Uuid, serverId: Long, version: Int): NetworkResult<Unit> =
        safeCall {
            client.delete("notes/$id") {
                parameter("version", version)
                parameter("id", serverId)
            }
        }
}
