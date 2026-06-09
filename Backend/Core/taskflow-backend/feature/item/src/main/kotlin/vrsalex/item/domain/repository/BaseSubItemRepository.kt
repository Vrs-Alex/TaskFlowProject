package vrsalex.item.domain.repository

import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
import vrsalex.core.database.AreaTable
import vrsalex.core.database.ItemTable
import vrsalex.core.database.utils.findOne
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.item.domain.model.SubItemCreate
import vrsalex.item.domain.model.SubItemUpdate
import kotlin.time.Instant
import kotlin.uuid.Uuid

abstract class BaseSubItemRepository<T, TCreate, TUpdate>(
    protected val itemRepository: ItemRepository,
    subTable: IdTable<Long>,
) : BaseSyncRepository<T, TCreate, TUpdate, ItemTable>(ItemTable)
        where T : SyncModel, TCreate : SubItemCreate, TUpdate : SubItemUpdate
{
    override val joinedTable: ColumnSet = ItemTable
        .leftJoin(AreaTable)
        .innerJoin(subTable)

    override suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<T> {
        val query = joinedTable.selectAll()
            .where { ItemTable.userId eq userId }
            .orderBy(ItemTable.id)
        if (lastSync != null) query.andWhere { ItemTable.updatedAt greaterEq lastSync }
        else query.andWhere { ItemTable.isDeleted eq false }

        val rows = query.toList()
        val tagsByItemId = itemRepository.loadTags(rows.map { it[ItemTable.id].value })

        return rows.map { it.toDomain(tagsByItemId) }
    }

    override suspend fun findById(id: Long, userId: Long): T? {
        val row = joinedTable.findOne {
            (ItemTable.id eq id) and (ItemTable.userId eq userId)
        } ?: return null
        val tagsByItemId = itemRepository.loadTags(listOf(row[ItemTable.id].value))
        return row.toDomain(tagsByItemId)
    }

    override suspend fun findByClientId(clientId: Uuid, userId: Long): T? {
        val row = joinedTable.findOne {
            (ItemTable.clientId eq clientId) and (ItemTable.userId eq userId)
        } ?: return null
        val tagsByItemId = itemRepository.loadTags(listOf(row[ItemTable.id].value))
        return row.toDomain(tagsByItemId)
    }

    override suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): T? {
        val row = joinedTable.findOne {
            (ItemTable.clientId eq clientId) and (ItemTable.id eq id) and
                    (ItemTable.userId eq userId)
        } ?: return null
        val tagsByItemId = itemRepository.loadTags(listOf(row[ItemTable.id].value))
        return row.toDomain(tagsByItemId)
    }

    abstract suspend fun ResultRow.toDomain(tagsByItemId: Map<Long, List<Uuid>>): T

    override suspend fun ResultRow.toDomain(): T = toDomain(emptyMap())


    abstract suspend fun getFullItem(id: Long, ownerId: Long): T

    abstract suspend fun insertSubDetails(itemId: Long, data: TCreate)

    abstract suspend fun updateSubDetails(itemId: Long, data: TUpdate)


    override suspend fun create(data: TCreate, _userId: Long): T = safeQuery("Не удалось создать заметку", logger) {
        val baseItem = itemRepository.create(data.base, _userId)
        insertSubDetails(baseItem.id, data)
        getFullItem(baseItem.id, _userId)
    }

    override suspend fun update(data: TUpdate, userId: Long): T = safeQuery("Не удалось обновить заметку", logger) {
        val baseUpdated = itemRepository.update(data.base, userId)
        updateSubDetails(baseUpdated.id, data)
        getFullItem(baseUpdated.id, userId)
    }

}