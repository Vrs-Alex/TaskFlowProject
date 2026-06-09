package vrsalex.tag.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.exception.AppException
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TagService(
    private val repository: TagRepository,
    private val transactionManager: TransactionManager,
    eventPublisher: EventPublisher,
): BaseSyncService<Tag, TagCreate, TagUpdate>(repository, transactionManager, eventPublisher){

    override val entityType: EntityType = EntityType.TAG

    override suspend fun create(data: TagCreate, userId: Long, userDeviceId: String): Tag = transactionManager.dbTransaction {
        if (repository.existByUserIdAndName(userId, data.name))
            throw AppException.Conflict("Тег с таким названием уже существует")

        super.create(data, userId, userDeviceId)
    }

    suspend fun search(filter: TagFilter, userId: Long): List<Tag> =
        transactionManager.dbTransaction { repository.search(filter, userId) }

}