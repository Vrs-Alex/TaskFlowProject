package com.vrsalex.network.public.api.realtime

import com.vrsalex.network.public.common.NetworkResult
import kotlinx.coroutines.flow.SharedFlow
import vrsalex.shared.api.realtime.RealtimeEventDto

interface RealtimeApi {

    val messages: SharedFlow<NetworkResult<RealtimeEventDto>>

    val connectionState: SharedFlow<ConnectionState>

    fun connect()

    suspend fun disconnect()

    suspend fun send(message: RealtimeEventDto)

}

