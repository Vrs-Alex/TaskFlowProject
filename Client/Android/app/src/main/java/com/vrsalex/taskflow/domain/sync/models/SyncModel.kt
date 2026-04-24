package com.vrsalex.taskflow.domain.sync.models

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncModel: SyncId {
    override val id: Uuid
    val serverId: Long?
    val updatedAt: Instant
    val version: Int
    val createdAt: Instant
    val isSynced: Boolean
}