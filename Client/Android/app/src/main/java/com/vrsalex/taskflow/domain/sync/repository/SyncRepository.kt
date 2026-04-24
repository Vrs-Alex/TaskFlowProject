package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.time.Instant

interface SyncRepository {

    suspend fun getLastSync(entity: SyncDbEntity): Instant?

    suspend fun setLastSync(entity: SyncDbEntity, lastSync: Instant)

}