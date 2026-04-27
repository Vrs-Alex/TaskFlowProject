package vrsalex.core.sync.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.event_bus.EventBusData
import vrsalex.core.exception.AppException
import vrsalex.core.model.EntityType
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.core.sync.repository.SyncRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

abstract class BaseSyncService<T, TCreate, TUpdate, TRepository>(
    private val repository: SyncRepository<T, TCreate, TUpdate>,
    private val transactionManager: TransactionManager,
    private val eventBus: EventBus
): SyncService<T, TCreate, TUpdate>
        where T : SyncModel, TCreate : SyncClientId, TUpdate : SyncUpdateModel,
              TRepository: SyncRepository<T, TCreate, TUpdate>{

    protected abstract val entityType: EntityType

    override suspend fun findById(id: Long, userId: Long): T = transactionManager.dbTransaction {
        repository.findById(id, userId)
            ?: throw AppException.NotFound("Заметка не найдена")
    }

    override suspend fun findByClientId(clientId: Uuid, userId: Long): T = transactionManager.dbTransaction {
        repository.findByClientId(clientId, userId)
            ?: throw AppException.NotFound("Заметка не найдена")
    }


    override suspend fun getChanges(lastSync: Instant?, userId: Long): List<T> = transactionManager.dbTransaction {
        repository.getChangesAfter(lastSync, userId)
    }

    override suspend fun create(data: TCreate, userId: Long, userDeviceId: String): T = transactionManager.dbTransaction {
        val exists = repository.findByClientId(data.clientId, userId)
        if (exists != null) {
            val isDeleted = repository.isDeleted(exists.id, userId)
            if (isDeleted) throw AppException.Gone("Заметка была удалена")
            return@dbTransaction exists
        }
        val result = repository.create(data, userId)
        eventBus.publish(EventBusData.EntityChanged(result.userId, result.id, entityType, result.updatedAt, userDeviceId))
        result
    }

    override suspend fun update(data: TUpdate, userId: Long, userDeviceId: String): T = transactionManager.dbTransaction {
        if (repository.isDeleted(data.id, userId)) {
            throw AppException.Gone("Заметка была удалена")
        }
        repository.findByClientId(data.clientId, userId)
            ?: throw AppException.NotFound("Заметка не найдена")
        val result = repository.update(data, userId)
        eventBus.publish(EventBusData.EntityChanged(result.userId, result.id, entityType, result.updatedAt, userDeviceId))
        result
    }

    override suspend fun delete(id: Long, clientId: Uuid, version: Int, userId: Long, userDeviceId: String): Boolean {
        val success = transactionManager.dbTransaction {
            repository.softDelete(id, clientId, version, userId)
        }
        if (success) {
            eventBus.publish(EventBusData.EntityChanged(userId, id, entityType, Clock.System.now().minus(15.seconds), userDeviceId))
        }
        else throw AppException.Conflict("Не удалось удалить заметку")

        return true
    }


}