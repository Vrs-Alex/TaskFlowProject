package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlin.time.Instant

interface SyncableRepository {

    suspend fun sync(lastSync: Instant? = null): Resource<Unit>

}