package com.vrsalex.network.public.api.item

import com.vrsalex.network.public.common.NetworkResult
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.conversion.ConvertItemRequest
import kotlin.uuid.Uuid

interface ItemApi {

    suspend fun convert(id: Uuid, request: ConvertItemRequest): NetworkResult<ItemDto>

}