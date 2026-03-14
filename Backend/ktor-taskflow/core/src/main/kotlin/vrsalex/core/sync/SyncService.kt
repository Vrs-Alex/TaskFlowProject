package vrsalex.core.sync

import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Сервис бизнес-логики для синхронизируемых сущностей.
 * Связывает контроллеры (API) с репозиториями, управляя транзакциями и исключениями.
 *
 * @param T Полная доменная модель.
 * @param CreateDto Данные для создания (обязательно с clientId).
 * @param UpdateDto Данные для обновления (обязательно с id и версией).
 */
interface SyncService<T, CreateDto, UpdateDto> {

    suspend fun existById(id: Long): Boolean

    suspend fun existByClientId(id: Uuid): Boolean

    suspend fun getById(id: Long, ownerId: Long): T

    suspend fun getByClientId(clientId: Uuid, ownerId: Long): T

    /** * Получает список изменений для синхронизации.
     * @param lastSync Метка времени последней успешной синхронизации клиента.
     * @return Список объектов, измененных или удаленных с момента [lastSync].
     */
    suspend fun getChanges(ownerId: Long, lastSync: Instant?): List<T>

    suspend fun create(data: CreateDto, ownerId: Long): Long

    suspend fun update(data: UpdateDto, ownerId: Long): T

    suspend fun delete(id: Long, ownerId: Long, version: Int): Boolean
}