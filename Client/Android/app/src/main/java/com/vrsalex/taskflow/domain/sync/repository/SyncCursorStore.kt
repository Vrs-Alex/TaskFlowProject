package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlin.time.Instant

interface SyncCursorStore {
    suspend fun getLastSync(entity: SyncEntity): Instant?
    suspend fun setLastSync(entity: SyncEntity, value: Instant)
}