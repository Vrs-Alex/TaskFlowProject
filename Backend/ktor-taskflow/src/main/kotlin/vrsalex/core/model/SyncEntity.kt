package vrsalex.core.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncEntity {
    val clientId: Uuid
    val createdAt: Instant
    val updatedAt: Instant
    val isDeleted: Boolean
    val version: Int
}