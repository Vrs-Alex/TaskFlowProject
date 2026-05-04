package vrsalex.tag.domain

import kotlinx.coroutines.flow.toList
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.exception.AppException
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TagService(
    private val repository: TagRepository,
    private val transactionManager: TransactionManager,
    eventBus: EventBus
): BaseSyncService<Tag, TagCreate, TagUpdate, TagRepository>(repository, transactionManager, eventBus){

    override val entityType: EntityType = EntityType.TAG

    override suspend fun create(data: TagCreate, userId: Long, userDeviceId: String): Tag = transactionManager.dbTransaction {
        if (repository.existByUserIdAndName(userId, data.name))
            throw AppException.Conflict("Тег с таким названием уже существует")

        super.create(data, userId, userDeviceId)
    }

    suspend fun search(filter: TagFilter, userId: Long): List<Tag> =
        transactionManager.dbTransaction { repository.search(filter, userId) }

}