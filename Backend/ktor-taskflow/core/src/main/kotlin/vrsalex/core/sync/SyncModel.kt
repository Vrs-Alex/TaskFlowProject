package vrsalex.core.sync

import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 *
 */
interface SyncModel: SyncClientId {
    val ownerId: Long
    val id: Long
    override val clientId: Uuid
    val version: Int
    val updatedAt: Instant
    val isDeleted: Boolean
}