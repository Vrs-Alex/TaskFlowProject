package vrsalex.core.sync

import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 *  Главная бизнес модель, которая реализует синхронизацию. Содержит все необходимые поля для синхронизации, такие как идентификатор, версия, время обновления и флаг удаления.
 *  Класс, который реализует этот интерфейс, также должен реализовать [[SyncClientId]] для обеспечения идемпотентности при синхронизации.
 */
interface SyncModel: SyncClientId {
    val ownerId: Long
    val id: Long
    override val clientId: Uuid
    val version: Int
    val updatedAt: Instant
    val isDeleted: Boolean
}