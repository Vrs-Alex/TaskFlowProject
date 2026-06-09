package com.vrsalex.network.internal.impl

import com.vrsalex.network.internal.ext.safeCall
import com.vrsalex.network.public.api.NotifyApi
import com.vrsalex.network.public.common.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vrsalex.shared.api.notify.RegisterDeviceRequest

class NotifyApiImpl(
    private val client: HttpClient
): NotifyApi {

    override suspend fun upsertDeviceInfo(data: RegisterDeviceRequest): NetworkResult<Unit> = safeCall {
        client.post("notify/device"){
            setBody(data)
        }
    }
}