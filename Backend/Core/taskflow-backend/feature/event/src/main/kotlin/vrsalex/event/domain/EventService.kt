package vrsalex.event.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService


class EventService(
    repository: EventRepository,
    transactionManager: TransactionManager,
    eventBus: EventBus
) : BaseSyncService<Event, EventCreate, EventUpdate, EventRepository>(repository, transactionManager, eventBus){

    override val entityType: EntityType = EntityType.EVENT

}