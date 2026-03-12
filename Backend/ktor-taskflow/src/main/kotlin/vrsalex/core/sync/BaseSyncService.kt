package vrsalex.core.sync

import kotlinx.coroutines.flow.toList
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.exception.AppException
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Базовая реализация сервиса синхронизации.
 * Обеспечивает выполнение всех операций в контексте транзакции и обрабатывает бизнес-исключения.
 *
 * @param T Доменная модель, реализующая [SyncModel].
 * @param CreateDto Данные для создания, реализующие [SyncClientId].
 * @param UpdateDto Данные для обновления, реализующие [SyncClientId].
 */
abstract class BaseSyncService<T : SyncModel, CreateDto: SyncClientId, UpdateDto: SyncClientId>(
    protected val repository: SyncRepository<T, CreateDto, UpdateDto>,
    private val transactionManager: TransactionManager
) : SyncService<T, CreateDto, UpdateDto> {

    override suspend fun existById(id: Long): Boolean = transactionManager.dbTransaction {
        repository.existsById(id)
    }

    override suspend fun existByClientId(id: Uuid): Boolean = transactionManager.dbTransaction {
        repository.existsByClientId(id)
    }

    override suspend fun getById(id: Long, ownerId: Long): T = transactionManager.dbTransaction {
        repository.findById(id, ownerId)
            ?: throw AppException.NotFound("Элемент с ID: $id не найден")
    }

    override suspend fun getByClientId(clientId: Uuid, ownerId: Long): T = transactionManager.dbTransaction {
        repository.findByClientId(clientId, ownerId)
            ?: throw AppException.NotFound("Элемент с ClientID: $clientId не найден")
    }

    override suspend fun getChanges(ownerId: Long, lastSync: Instant?): List<T> = transactionManager.dbTransaction {
        repository.findChangesAfter(ownerId, lastSync).toList()
    }

    /**
     * Идемпотентное создание записи.
     * Если запись с таким [data.clientId] уже существует, возвращает её текущий ID
     * вместо создания дубликата, что предотвращает ошибки при повторных запросах.
     */
    override suspend fun create(data: CreateDto, ownerId: Long): Long = transactionManager.dbTransaction {
        val existing = repository.findByClientId(data.clientId, ownerId)
        if (existing != null) {
            return@dbTransaction existing.id
        }
        repository.create(data, ownerId)
    }

    override suspend fun update(data: UpdateDto, ownerId: Long): Boolean = transactionManager.dbTransaction {
        val success = repository.update(data, ownerId)
        if (!success) {
            throw AppException.Conflict("Не удалось обновить: версия не совпала или запись не найдена")
        }
        true
    }

    override suspend fun delete(id: Long, ownerId: Long, version: Int): Boolean = transactionManager.dbTransaction {
        val success = repository.softDelete(id, ownerId, version)
        if (!success) {
            throw AppException.Conflict("Не удалось удалить: конфликт версий")
        }
        true
    }
}