package vrsalex.core.sync.repository

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncRepository<T, TCreate, TUpdate> {

    suspend fun existsById(id: Long, userId: Long): Boolean
    suspend fun existsByClientId(clientId: Uuid, userId: Long): Boolean
    suspend fun existsByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Boolean

    suspend fun isDeleted(id: Long, userId: Long): Boolean

    suspend fun findById(id: Long, userId: Long): T?
    suspend fun findByClientId(clientId: Uuid, userId: Long): T?
    suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): T?

    suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<T>

    suspend fun create(data: TCreate, userId: Long): T

    /**
     * Если не найден элемент, то выбрасывается исключение [[vrsalex.core.exception.AppException.BadRequest]]
     */
    suspend fun update(data: TUpdate, userId: Long): T
    suspend fun softDelete(clientId: Uuid, version: Int, userId: Long): Boolean

}