package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlin.time.Instant

interface SyncableRepository {

    suspend fun sync(lastSync: Instant? = null): Resource<Unit>

}