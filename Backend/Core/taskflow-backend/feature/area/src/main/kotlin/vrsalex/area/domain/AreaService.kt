package vrsalex.area.domain

import kotlinx.coroutines.flow.toList
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class AreaService(
    private val repository: AreaRepository,
    private val transactionManager: TransactionManager,
    eventBus: EventBus
): BaseSyncService<Area, AreaCreate, AreaUpdate, AreaRepository>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.AREA

    suspend fun search(filter: AreaFilter, userId: Long): List<Area> = transactionManager.dbTransaction {
        repository.search(filter, userId)
    }

}