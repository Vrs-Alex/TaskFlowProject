package com.vrsalex.taskflow.data.local.db.entity

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncDbModel {
    val id: Uuid
    val serverId: Long?
    val updatedAt: Instant
    val version: Int
    val createdAt: Instant
    val isSynced: Boolean
}