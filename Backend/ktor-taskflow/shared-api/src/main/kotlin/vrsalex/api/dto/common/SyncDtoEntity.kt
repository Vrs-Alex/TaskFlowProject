package vrsalex.api.dto.common

import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Родительский интерфейс для сущности, которая реализует синхронизацию.
 */
interface SyncDtoEntity {
    val id: Long
    val clientId: Uuid
    val version: Int
    val updatedAt: Instant
}