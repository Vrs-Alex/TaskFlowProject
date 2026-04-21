package vrsalex.shared.api.common

import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncDto {
    val id: Long
    val clientId: Uuid
    val version: Int
    val updatedAt: Instant
}