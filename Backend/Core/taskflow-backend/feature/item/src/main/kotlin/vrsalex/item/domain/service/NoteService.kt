package vrsalex.item.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.repository.NoteRepository

class NoteService(
    repository: NoteRepository,
    transactionManager: TransactionManager,
    private val eventPublisher: EventPublisher,
) : BaseSyncService<Item, ItemCreate, ItemUpdate>(repository, transactionManager, eventPublisher) {

    override val entityType: EntityType = EntityType.NOTE

    override suspend fun create(data: ItemCreate, userId: Long, userDeviceId: String): Item {
        return super.create(data, userId, userDeviceId).also {
            eventPublisher.publish(AppEvent.PushNotification(userId, userDeviceId, data.name, data.description ?: ""))
        }
    }

}
