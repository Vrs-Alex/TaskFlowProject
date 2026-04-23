package vrsalex.area.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class AreaService(
    repository: AreaRepository,
    transactionManager: TransactionManager,
    eventBus: EventBus
): BaseSyncService<Area, AreaCreate, AreaUpdate, AreaRepository>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.AREA

}