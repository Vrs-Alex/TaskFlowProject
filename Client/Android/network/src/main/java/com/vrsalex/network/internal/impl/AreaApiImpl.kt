package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest
import vrsalex.shared.api.common.ModelDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

internal class AreaApiImpl(
    private val client: HttpClient
): AreaApi {

    override suspend fun get(lastSync: Instant?): NetworkResult<List<ModelDto<AreaDto>>> =
        safeCall {
            client.get("areas/sync") {
                lastSync?.let {
                    parameter("lastSync", it.toString())
                }
            }
        }

    override suspend fun create(data: AreaCreateRequest): NetworkResult<AreaDto> =
        safeCall {
            client.post("areas") {
                setBody(data)
            }
        }

    override suspend fun update(data: AreaUpdateRequest): NetworkResult<AreaDto> =
        safeCall {
            client.patch("areas") {
                setBody(data)
            }
        }

    override suspend fun delete(
        id: Uuid,
        serverId: Long,
        version: Int
    ): NetworkResult<Unit> = safeCall {
        client.delete("areas/$id"){
            parameter("version", version)
            parameter("id", serverId)
        }
    }



}