package vrsalex.api.dto.common

import kotlin.uuid.Uuid

/**
 * Родительский интерфейс для сущности, который создает сущность [SyncDtoEntity].
 * Предоставляет [clientId] который создается на клиенте (Offline-first).
 */
interface SyncCreateEntity {
    val clientId: Uuid
}