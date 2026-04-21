package vrsalex.core.sync.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.exception.AppException
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.core.sync.repository.SyncRepository
import kotlin.time.Instant
import kotlin.uuid.Uuid

abstract class BaseSyncService<T, TCreate, TUpdate, TRepository>(
    private val repository: SyncRepository<T, TCreate, TUpdate>,
    private val transactionManager: TransactionManager
): SyncService<T, TCreate, TUpdate>
        where T : SyncModel, TCreate : SyncClientId, TUpdate : SyncUpdateModel,
              TRepository: SyncRepository<T, TCreate, TUpdate>{


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

    override suspend fun create(data: TCreate, userId: Long): T = transactionManager.dbTransaction {
        val exists = repository.findByClientId(data.clientId, userId)
        if (exists != null) {
            val isDeleted = repository.isDeleted(exists.id, userId)
            if (isDeleted) throw AppException.Gone("Заметка была удалена")
            return@dbTransaction exists
        }
        repository.create(data, userId)
    }

    override suspend fun update(data: TUpdate, userId: Long): T = transactionManager.dbTransaction {
        if (repository.isDeleted(data.id, userId)) {
            throw AppException.Gone("Заметка была удалена")
        }
        repository.findByClientId(data.clientId, userId)
            ?: throw AppException.NotFound("Заметка не найдена")
        repository.update(data, userId)
    }

    override suspend fun delete(clientId: Uuid, version: Int, userId: Long): Boolean {
        val success = transactionManager.dbTransaction {
            repository.softDelete(clientId, version, userId)
        }
        if (success) { }
        else throw AppException.Conflict("Не удалось удалить заметку")

        return true
    }


}