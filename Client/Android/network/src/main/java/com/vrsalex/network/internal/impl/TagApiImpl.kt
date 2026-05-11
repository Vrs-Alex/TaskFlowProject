package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.TagApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal class TagApiImpl(
    private val client: HttpClient
): TagApi {


    override suspend fun getById(id: Uuid): NetworkResult<TagDto?> =
        safeCall {
            client.get("tags/${id}")
        }

    override suspend fun sync(lastSync: Instant?): NetworkResult<List<ModelDto<TagDto>>> =
        safeCall {
            client.get("tags/sync"){
                lastSync?.let {
                    parameter("lastSync", it.toString())
                }
            }
        }

    override suspend fun syncItem(id: Uuid): NetworkResult<ModelDto<TagDto>> =
        safeCall {
            client.get("tags/sync/$id")
        }

    override suspend fun create(data: TagCreateRequest): NetworkResult<TagDto> =
        safeCall {
            client.post("tags"){
                setBody(data)
            }
        }

    override suspend fun update(data: TagUpdateRequest): NetworkResult<TagDto> =
        safeCall {
            client.patch("tags"){
                setBody(data)
            }
        }

    override suspend fun delete(
        id: Uuid,
        serverId: Long,
        version: Int
    ): NetworkResult<Unit> = safeCall {
        client.delete("tags/$id") {
            parameter("version", version)
            parameter("id", serverId)
        }
    }

    override suspend fun findByFilter(
        name: String?,
        nameExact: Boolean,
        color: String?
    ): NetworkResult<List<TagDto>> = safeCall {
        client.get("tags") {
            name?.let      { parameter("name", it) }
            if (nameExact) parameter("nameExact", true)
            color?.let     { parameter("color", it) }
        }
    }

}