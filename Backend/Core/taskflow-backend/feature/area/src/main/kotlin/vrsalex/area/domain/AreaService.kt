package vrsalex.area.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class AreaService(
    private val repository: AreaRepository,
    private val transactionManager: TransactionManager,
    eventPublisher: EventPublisher,
): BaseSyncService<Area, AreaCreate, AreaUpdate>(repository, transactionManager, eventPublisher) {

    override val entityType: EntityType = EntityType.AREA

    suspend fun search(filter: AreaFilter, userId: Long): List<Area> = transactionManager.dbTransaction {
        repository.search(filter, userId)
    }

}