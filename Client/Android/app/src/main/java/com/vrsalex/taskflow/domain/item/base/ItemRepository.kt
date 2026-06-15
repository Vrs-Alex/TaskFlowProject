package com.vrsalex.taskflow.domain.item.base

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Единый плоский контракт репозитория item: CRUD + синхронизация.
 *
 * Дата-/тип-специфичные запросы (getByDate, getOverdue, getArchived) живут на конкретных
 * наследниках (TaskRepository / EventRepository), а не здесь — они применимы не ко всем типам.
 */
interface ItemRepository<T, TCreate, TUpdate> {

    fun get(): Flow<List<T>>

    fun getById(id: Uuid): Flow<T?>

    suspend fun create(data: TCreate)

    suspend fun update(data: TUpdate)

    suspend fun delete(id: Uuid)

    suspend fun sync(lastSync: Instant? = null): Resource<Unit>

    suspend fun syncItem(id: Uuid): Resource<Unit>
}
