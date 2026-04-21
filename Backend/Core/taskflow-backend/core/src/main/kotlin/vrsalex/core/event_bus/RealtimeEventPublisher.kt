package vrsalex.core.event_bus



/**
 * Абстрактный провайдер уведомлений.
 * Позволяет отправлять события пользователю, не привязываясь к транспорту (WS, FCM, и т.д.)
 */
interface RealtimeEventPublisher {

    suspend fun sendEvent(userId: Long, event: RealtimeEventDto)

    suspend fun broadcast(event: RealtimeEventDto)

}