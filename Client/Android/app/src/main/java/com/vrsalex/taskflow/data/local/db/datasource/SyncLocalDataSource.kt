package com.vrsalex.taskflow.data.local.db.datasource

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncLocalDataSource {

    suspend fun markSynced(id: Uuid, newId: Uuid, serverId: Long, version: Int, updatedAt: Instant)

}