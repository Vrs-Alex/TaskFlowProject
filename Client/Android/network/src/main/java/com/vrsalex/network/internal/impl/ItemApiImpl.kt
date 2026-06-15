package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.item.ItemApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.conversion.ConvertItemRequest
import kotlin.uuid.Uuid

class ItemApiImpl(
    private val client: HttpClient
): ItemApi {
    override suspend fun convert(
        id: Uuid,
        request: ConvertItemRequest
    ): NetworkResult<ItemDto> = safeCall {
        client.post("items/$id/convert") { setBody(request) }
    }
}