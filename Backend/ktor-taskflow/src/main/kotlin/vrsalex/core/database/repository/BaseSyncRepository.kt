package vrsalex.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.entity.SyncTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.core.sync.SyncModel
import vrsalex.core.sync.SyncRepository
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Базовая реализация репозитория с поддержкой синхронизации.
 *
 * @param T Доменная модель, реализующая [SyncModel].
 * @param Table Объект таблицы Exposed, реализующий [IdTable] и [SyncTable].
 * @param CreateDto DTO для создания новой записи.
 * @param UpdateDto DTO для обновления существующей записи.
 * * Ограничение `where Table : IdTable<Long>, Table : SyncTable` гарантирует,
 * что таблица имеет первичный ключ Long и колонки синхронизации.
 */
abstract class BaseSyncRepository<T : SyncModel, Table, CreateDto, UpdateDto>(
    protected val table: Table
) : SyncRepository<T, CreateDto, UpdateDto> where Table : IdTable<Long>, Table : SyncTable {


    abstract fun ResultRow.toDomain(): T

    override suspend fun existsById(id: Long): Boolean = table.exists {
        table.id eq id
    }

    override suspend fun existsByClientId(id: Uuid): Boolean = table.exists {
        table.clientId eq id
    }

    override suspend fun findById(id: Long, ownerId: Long): T? =
        table.findOne {
            (table.id eq id) and (table.ownerId eq ownerId)
        }?.toDomain()

    override suspend fun findByClientId(clientId: Uuid, ownerId: Long): T? =
        table.findOne {
            (table.clientId eq clientId) and (table.ownerId eq ownerId)
        }?.toDomain()

    /**
     * Реализация инкрементальной синхронизации:
     * - Если [lastSync] null: отдаем все актуальные (не удаленные) записи.
     * - Если [lastSync] указан: отдаем всё, что изменилось или было удалено после этой даты.
     */
    override suspend fun findChangesAfter(ownerId: Long, lastSync: Instant?): Flow<T> {
        val query = table.selectAll()
            .where { (table.ownerId eq ownerId) and (table.isDeleted eq false) }

        if (lastSync != null) {
            query.andWhere { table.updatedAt greater lastSync }
        }

        return query.map { it.toDomain() }
    }

    /**
     * Выполняет "мягкое удаление".
     * Использует версию [currentVersion] для предотвращения перезаписи,
     * если данные были изменены другим устройством (Optimistic Lock).
     */
    override suspend fun softDelete(id: Long, ownerId: Long, currentVersion: Int): Boolean =
        table.update({
            (table.id eq id) and (table.ownerId eq ownerId) and (table.version eq currentVersion)
        }) {
            it[isDeleted] = true
            it[version] = currentVersion + 1
        } > 0
}