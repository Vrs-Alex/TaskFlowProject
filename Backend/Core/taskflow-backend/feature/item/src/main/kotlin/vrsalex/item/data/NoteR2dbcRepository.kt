package vrsalex.item.data

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ColumnSet
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.innerJoin
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.AreaTable
import vrsalex.core.database.ItemTable
import vrsalex.core.database.ItemTagsTable
import vrsalex.core.database.TagTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemStatus
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.repository.NoteRepository
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteR2dbcRepository: ItemR2dbcRepository(), NoteRepository {

    override suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<Item> {
        val query = joinedTable.selectAll().where {
            (table.userId eq userId) and (table.type eq ItemType.NOTE.name)
        }.orderBy(table.id)

        if (lastSync != null) query.andWhere { table.updatedAt greaterEq  lastSync }
        else query.andWhere { table.isDeleted eq false }

        return query.map { it.toDomain() }.toList()
    }

    override suspend fun existsByClientId(clientId: Uuid, userId: Long): Boolean =
        joinedTable.exists { (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun existsById(id: Long, userId: Long): Boolean =
        joinedTable.exists {( table.id eq id) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun existsByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Boolean =
        joinedTable.exists { (table.id eq id) and (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun findByClientId(clientId: Uuid, userId: Long): Item? =
        joinedTable.findOne { (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }?.toDomain()

    override suspend fun findById(id: Long, userId: Long): Item? =
        joinedTable.findOne { (table.id eq id) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }?.toDomain()

    override suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Item? =
        joinedTable.findOne { (table.id eq id) and (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }?.toDomain()

}