package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.model.ISyncModel
import com.vrsalex.taskflow.domain.sync.model.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.model.ISyncModelUpdate
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncRepository<T : ISyncModel, TCreate : ISyncModelCreate, TUpdate : ISyncModelUpdate> {
    fun observeAll(): Flow<List<T>>
    fun observeById(id: Uuid): Flow<T?>
    suspend fun create(data: TCreate)
    suspend fun update(data: TUpdate)
    suspend fun delete(id: Uuid)
    suspend fun sync(lastSync: Instant? = null): Resource<Unit>
    suspend fun syncById(id: Uuid): Resource<Unit>

    /**
     * Отправляет локальные несинхронизированные изменения на сервер.
     * По умолчанию — no-op; переопределяется по мере подключения [SyncPusher] к конкретному репозиторию.
     */
    suspend fun push(): Resource<Unit> = Resource.Success(Unit)
}