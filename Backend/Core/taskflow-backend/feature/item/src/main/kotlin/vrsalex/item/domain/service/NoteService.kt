package vrsalex.item.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.item.domain.repository.NoteRepository

class NoteService(
    repository: NoteRepository,
    transactionManager: TransactionManager,
    eventBus: EventBus
) : BaseSyncService<Item, ItemCreate, ItemUpdate, ItemRepository>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.NOTE



}
