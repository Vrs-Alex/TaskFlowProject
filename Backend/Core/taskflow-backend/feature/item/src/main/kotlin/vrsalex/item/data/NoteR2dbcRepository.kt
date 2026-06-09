package vrsalex.item.data

import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
import vrsalex.core.database.ItemTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.repository.NoteRepository
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteR2dbcRepository: ItemR2dbcRepository(), NoteRepository {


    override suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<Item> {
        val query = joinedTable.selectAll()
            .where { (ItemTable.userId eq userId) and (ItemTable.type eq ItemType.NOTE.name) }
            .orderBy(ItemTable.id)
        if (lastSync != null) query.andWhere { ItemTable.updatedAt greaterEq lastSync }
        else query.andWhere { ItemTable.isDeleted eq false }

        val rows = query.toList()
        val tagsByItemId = loadTags(rows.map { it[ItemTable.id].value })

        return rows.map { it.toItem(tagsByItemId) }
    }

    override suspend fun existsByClientId(clientId: Uuid, userId: Long): Boolean =
        joinedTable.exists { (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun existsById(id: Long, userId: Long): Boolean =
        joinedTable.exists {( table.id eq id) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun existsByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Boolean =
        joinedTable.exists { (table.id eq id) and (table.clientId eq clientId) and (table.userId eq userId) and (table.type eq ItemType.NOTE.name)  }

    override suspend fun findByClientId(clientId: Uuid, userId: Long): Item? {
        val row = joinedTable.findOne {
            (ItemTable.clientId eq clientId) and
                    (ItemTable.userId eq userId) and
                    (table.type eq ItemType.NOTE.name)
        } ?: return null
        val tagsByItemId = loadTags(listOf(row[ItemTable.id].value))
        return row.toItem(tagsByItemId)
    }

    override suspend fun findById(id: Long, userId: Long): Item? {
        val row = joinedTable.findOne {
            (ItemTable.id eq id) and
                    (ItemTable.userId eq userId) and
                    (table.type eq ItemType.NOTE.name)
        } ?: return null
        val tagsByItemId = loadTags(listOf(row[ItemTable.id].value))
        return row.toItem(tagsByItemId)
    }

    override suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): Item? {
        val row = joinedTable.findOne {
            (ItemTable.clientId eq clientId) and
                    (ItemTable.id eq id) and
                    (ItemTable.userId eq userId) and
                    (table.type eq ItemType.NOTE.name)
        } ?: return null
        val tagsByItemId = loadTags(listOf(row[ItemTable.id].value))
        return row.toItem(tagsByItemId)
    }

}