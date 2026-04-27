package com.vrsalex.taskflow.domain.sync.repository

import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface CrudRepository<T, TCreate, TUpdate> : SyncableRepository {
    fun get(): Flow<List<T>>
    fun getById(id: Uuid): Flow<T?>
    suspend fun create(data: TCreate)
    suspend fun update(data: TUpdate)
    suspend fun delete(id: Uuid)
}