package vrsalex.realtime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import vrsalex.core.event_bus.EventBusData
import vrsalex.core.event_bus.EventBus
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.core.model.toDto
import vrsalex.shared.api.realtime.RealtimeEventDto

class RealtimeBridge(
    private val eventBus: EventBus,
    private val realtimeEventPublisher: RealtimeEventPublisher,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    init {
        scope.launch {
            eventBus.events.collect { event ->
                when (event) {
                    is EventBusData.EntityChanged -> {
                        realtimeEventPublisher.sendEvent(
                            userId = event.userId,
                            event = RealtimeEventDto.EntityChanged(
                                event.entityId,
                                event.entityType.toDto(),
                                event.time
                            ),
                            excludeDeviceId = event.userDeviceId
                        )
                    }

                    is EventBusData.Logout -> {
                        realtimeEventPublisher.sendEvent(event.userId, RealtimeEventDto.Logout, excludeDeviceId = "")
                    }
                }
            }
        }
    }
}