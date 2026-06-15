package com.vrsalex.taskflow.domain.sync

import kotlin.time.Instant

interface SyncCursorStore {
    suspend fun getLastSync(entity: SyncEntity): Instant?
    suspend fun setLastSync(entity: SyncEntity, value: Instant)
}