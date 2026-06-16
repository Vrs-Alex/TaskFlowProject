package com.vrsalex.taskflow.data.local.db.mapper

import com.vrsalex.taskflow.data.local.db.entity.SyncColumns
import com.vrsalex.taskflow.domain.sync.model.SyncModel
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

fun SyncColumns.toSyncModel(id: Uuid): SyncModel =
    SyncModel(id, serverId, version, updatedAt, createdAt, isDeleted, isSynced)

fun newLocalSync(now: Instant = Clock.System.now()): SyncColumns =
    SyncColumns(serverId = null, version = 1, updatedAt = now, createdAt = now, isDeleted = false, isSynced = false)
