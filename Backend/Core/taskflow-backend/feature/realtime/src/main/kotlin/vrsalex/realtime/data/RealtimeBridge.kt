package vrsalex.realtime.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import vrsalex.core.event_bus.EventBus
import vrsalex.core.event_bus.EventBusData
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.core.model.toDto
import vrsalex.notify.domain.NotifyRepository
import vrsalex.shared.api.realtime.RealtimeEventDto.EntityChanged
import vrsalex.shared.api.realtime.RealtimeEventDto.Logout

class RealtimeBridge(
    private val eventBus: EventBus,
    private val realtimeEventPublisher: RealtimeEventPublisher,
    private val notifyRepository: NotifyRepository,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    init {
        scope.launch {
            eventBus.events.collect { event ->
                when (event) {
                    is EventBusData.EntityChanged -> {
                        realtimeEventPublisher.sendEvent(
                            userId = event.userId,
                            event = EntityChanged(
                                event.entityId,
                                event.entityType.toDto(),
                                event.time
                            ),
                            excludeDeviceId = event.userDeviceId
                        )
                    }

                    is EventBusData.PushNotifications -> {
                        notifyRepository.sendPush(
                            title = event.title,
                            description = event.description,
                            userId = event.userId,
                            excludeDeviceId = event.excludeDeviceId
                        )
                    }

                    is EventBusData.Logout -> {
                        realtimeEventPublisher.sendEvent(event.userId, Logout, excludeDeviceId = "")
                    }

                }
            }
        }
    }
}