package vrsalex.event.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.event_bus.EventBusData
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService


class EventService(
    repository: EventRepository,
    transactionManager: TransactionManager,
    private val eventBus: EventBus
) : BaseSyncService<Event, EventCreate, EventUpdate>(repository, transactionManager, eventBus){

    override val entityType: EntityType = EntityType.EVENT

    override suspend fun create(data: EventCreate, userId: Long, userDeviceId: String): Event {
        return super.create(data, userId, userDeviceId).also {
            eventBus.publish(EventBusData.PushNotifications(userId, userDeviceId, data.base.name, data.base.description ?: ""))
        }
    }

}