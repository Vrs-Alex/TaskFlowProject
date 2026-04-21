package vrsalex.shared.api.common

import kotlin.uuid.Uuid

/**
 * Родительский интерфейс для сущности, которая обновляет [[SyncDto]].
 */
interface SyncUpdateDto {
    val id: Long
    val clientId: Uuid
    val version: Int
}