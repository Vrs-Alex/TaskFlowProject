package dto.common

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncDtoEntity {
    val id: Long
    val clientId: Uuid
    val version: Int
    val updatedAt: Instant
}