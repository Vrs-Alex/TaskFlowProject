package com.vrsalex.taskflow.data.local.db.entity

import kotlin.time.Instant

data class SyncColumns(
    val serverId: Long?,
    val version: Int,
    val updatedAt: Instant,
    val createdAt: Instant,
    val isDeleted: Boolean,
    val isSynced: Boolean,
)
