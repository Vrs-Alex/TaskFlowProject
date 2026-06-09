package vrsalex.event.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService


class EventService(
    repository: EventRepository,
    transactionManager: TransactionManager,
    private val eventPublisher: EventPublisher,
) : BaseSyncService<Event, EventCreate, EventUpdate>(repository, transactionManager, eventPublisher){

    override val entityType: EntityType = EntityType.EVENT

    override suspend fun create(data: EventCreate, userId: Long, userDeviceId: String): Event {
        return super.create(data, userId, userDeviceId).also {
            eventPublisher.publish(AppEvent.PushNotification(userId, userDeviceId, data.base.name, data.base.description ?: ""))
        }
    }

}