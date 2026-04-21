package vrsalex.core.sync.model

import kotlin.uuid.Uuid

/**
 * Модель для обновления сущности, которая реализует синхронизацию.
 * Содержит идентификатор и версию, чтобы клиент не мог обновить то, что уже обновлено с другого уст-ва
 * Класс, который реализует этот интерфейс, также должен реализовать [[SyncClientId]] для обеспечения идемпотентности при синхронизации.
 */
interface SyncUpdateModel: SyncClientId {
    override val clientId: Uuid
    val id: Long
    val version: Int
}