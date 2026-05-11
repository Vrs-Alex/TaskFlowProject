package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncableRepository {

    suspend fun sync(lastSync: Instant? = null): Resource<Unit>

    suspend fun syncItem(id: Uuid): Resource<Unit>

}