package vrsalex.core.sync

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncModel: SyncClientId {
    val userId: Long
    val id: Long
    override val clientId: Uuid
    val updatedAt: Instant
    val version: Int
    val isDeleted: Boolean
}