package com.vrsalex.taskflow.domain.sync

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class SyncModel(
    val id: Uuid,
    val serverId: Long?,
    val version: Int,
    val updatedAt: Instant,
    val createdAt: Instant,
    val isDeleted: Boolean,
    val isSynced: Boolean
)

data class SyncModelCreate(
    val id: Uuid
)

data class SyncModelUpdate(
    val id: Uuid,
    val serverId: Long,
    val version: Int
)