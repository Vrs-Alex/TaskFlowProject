package vrsalex.shared.api.common

import kotlin.uuid.Uuid

/**
 * Родительский интерфейс для сущности, который создает сущность [SyncDto].
 * Предоставляет [clientId] который создается на клиенте (Offline-first).
 */
interface SyncCreateDto {
    val clientId: Uuid
}