package vrsalex.core.event_bus.domain

interface EventPublisher {

    suspend fun publish(event: AppEvent)

}