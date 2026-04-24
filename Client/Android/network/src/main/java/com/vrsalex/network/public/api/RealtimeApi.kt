package com.vrsalex.network.public.api

import com.vrsalex.network.public.common.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import vrsalex.shared.api.realtime.RealtimeEventDto

interface RealtimeApi {

    val messages: SharedFlow<NetworkResult<RealtimeEventDto>>

    suspend fun send(message: RealtimeEventDto)

    fun connect()

    suspend fun disconnect()

}