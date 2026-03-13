package vrsalex.core.sync

/**
 * Модель для обновления сущности которая реализует синхронизацию.
 * Содержит идентификатор и версию, чтобы клиент не мог обновить то, что уже обновлено с другого уст-ва
 */
interface SyncUpdateModel {
    val id: Long
    val version: Int
}