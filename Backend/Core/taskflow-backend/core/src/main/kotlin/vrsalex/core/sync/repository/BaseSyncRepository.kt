package vrsalex.core.sync.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import org.slf4j.LoggerFactory
import vrsalex.core.database.SyncTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

abstract class BaseSyncRepository<T, TCreate, TUpdate, Table>(
    protected val table: Table
): SyncRepository<T, TCreate, TUpdate> where Table : SyncTable, Table : IdTable<Long> {

    protected val logger = LoggerFactory.getLogger(this::class.java)!!

    abstract fun ResultRow.toDomain(): T




    override suspend fun existsById(id: Long, userId: Long): Boolean =
        table.exists {( table.id eq id) and (table.userId eq userId) }

    override suspend fun existsByClientId(clientId: Uuid, userId: Long): Boolean =
        table.exists { (table.clientId eq clientId) and (table.userId eq userId) }

    override suspend fun existsByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Boolean =
        table.exists { (table.id eq id) and (table.clientId eq clientId) and (table.userId eq userId) }



    override suspend fun isDeleted(id: Long, userId: Long): Boolean =
        table.exists { (table.id eq id) and (table.userId eq userId) and (table.isDeleted eq true) }



    override suspend fun findById(id: Long, userId: Long): T? =
        table.findOne { (table.id eq id) and (table.userId eq userId) }?.toDomain()

    override suspend fun findByClientId(clientId: Uuid, userId: Long): T? =
        table.findOne { (table.clientId eq clientId) and (table.userId eq userId) }?.toDomain()

    override suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): T? =
        table.findOne { (table.id eq id) and (table.clientId eq clientId) and (table.userId eq userId) }?.toDomain()


    /**
     * Возвращает список изменений для пользователя [userId], произошедших после метки времени [lastSync].
     * В BaseSubItemRepository код дублируется (нарушение DRY)
     */
    override suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<T> {
        val query = table.selectAll().where { table.userId eq userId and (table.isDeleted eq false) }.orderBy(table.id)

        if (lastSync != null) query.andWhere { table.updatedAt greaterEq  lastSync }

        return query.map { it.toDomain() }.toList()
    }

    override suspend fun softDelete(clientId: Uuid, version: Int, userId: Long): Boolean =
        table.update({
            (table.clientId eq clientId) and (table.userId eq userId) and (table.version eq version)
        }) {
            it[table.isDeleted] = true
            it[table.version] = version + 1
            it[table.updatedAt] = Clock.System.now()
        } > 0
}