package vrsalex.core.sync

import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Базовый контракт репозитория для сущностей с поддержкой синхронизации.
 *
 * @param T Тип основной доменной модели (например, Area).
 * @param CreateDto Тип данных для создания (должен содержать clientId, к примеру AreaCreate).
 * @param UpdateDto Тип данных для обновления (должен содержать id и версию, AreaUpdate).
 */
interface SyncRepository<T, CreateDto, UpdateDto> {

    suspend fun existsById(id: Long): Boolean

    suspend fun existsByClientId(id: Uuid): Boolean

    suspend fun findById(id: Long, ownerId: Long): T?

    suspend fun findByClientId(clientId: Uuid, ownerId: Long): T?

    /** * Возвращает поток изменений для пользователя [ownerId],
     * произошедших после метки времени [lastSync].
     */
    suspend fun findChangesAfter(ownerId: Long, lastSync: Instant?): Flow<T>


    suspend fun create(data: CreateDto, ownerId: Long): Long

    suspend fun update(data: UpdateDto, ownerId: Long): Boolean

    suspend fun softDelete(id: Long, ownerId: Long, currentVersion: Int): Boolean
}