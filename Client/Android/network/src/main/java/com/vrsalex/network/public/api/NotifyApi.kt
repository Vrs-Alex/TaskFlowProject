package com.vrsalex.network.public.api

import com.vrsalex.network.public.common.NetworkResult
import vrsalex.shared.api.notify.RegisterDeviceRequest

interface NotifyApi {

    suspend fun upsertDeviceInfo(data: RegisterDeviceRequest): NetworkResult<Unit>

}