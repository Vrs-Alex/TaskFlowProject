package vrsalex.api.dto.common

import kotlin.uuid.Uuid

/**
 * Родительский интерфейс для сущности, которая обновляет [[SyncDtoEntity]].
 */
interface SyncUpdateEntity {
    val id: Long
    val clientId: Uuid
    val version: Int
}