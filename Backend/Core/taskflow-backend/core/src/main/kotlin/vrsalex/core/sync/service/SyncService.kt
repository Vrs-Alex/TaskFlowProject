package vrsalex.core.sync.service

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncService<T, TCreate, TUpdate> {

    suspend fun findById(id: Long, userId: Long): T

    suspend fun findByClientId(clientId: Uuid, userId: Long): T

    suspend fun getChanges(lastSync: Instant?, userId: Long): List<T>

    suspend fun create(data: TCreate, userId: Long): T

    suspend fun update(data: TUpdate, userId: Long): T

    suspend fun delete(id: Long, clientId: Uuid, version: Int, userId: Long): Boolean

}