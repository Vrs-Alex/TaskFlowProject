package com.vrsalex.network.public.api

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.network.public.common.SyncApi
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest

interface AreaApi: SyncApi<AreaDto, AreaCreateRequest, AreaUpdateRequest> {

    suspend fun findByFilter(
        name: String? = null,
        nameExact: Boolean = false,
        color: String? = null
    ): NetworkResult<List<AreaDto>>

}