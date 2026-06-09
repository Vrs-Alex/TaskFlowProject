package vrsalex.realtime.domain

import vrsalex.shared.api.realtime.RealtimeEventDto

interface RealtimeEventPublisher {

    suspend fun sendEvent(userId: Long, event: RealtimeEventDto, sourceDeviceId: String)

    suspend fun broadcast(event: RealtimeEventDto)

}