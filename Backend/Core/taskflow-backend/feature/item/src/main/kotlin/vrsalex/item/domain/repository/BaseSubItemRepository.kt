package vrsalex.item.domain.repository

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
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

    protected val fullJoin = ItemTable innerJoin subTable


    abstract suspend fun getFullItem(id: Long, ownerId: Long): T

    abstract suspend fun insertSubDetails(itemId: Long, data: TCreate)

    abstract suspend fun updateSubDetails(itemId: Long, data: TUpdate)


    override suspend fun findById(id: Long, userId: Long): T? =
        fullJoin.findOne { (ItemTable.id eq id) and (ItemTable.userId eq userId) }
            ?.toDomain()

    override suspend fun findByClientId(clientId: Uuid, userId: Long): T? =
        fullJoin.findOne { (ItemTable.clientId eq clientId) and (ItemTable.userId eq userId) }
            ?.toDomain()

    override suspend fun findByIdAndClientId(id: Long, clientId: Uuid, userId: Long): T? =
        fullJoin.findOne { (ItemTable.id eq id) and (ItemTable.clientId eq clientId) and (ItemTable.userId eq userId) }
            ?.toDomain()


    override suspend fun getChangesAfter(lastSync: Instant?, userId: Long): List<T> {
        val query = fullJoin.selectAll().where { ItemTable.userId eq userId }.orderBy(table.id)

        if (lastSync != null) query.andWhere { table.updatedAt greaterEq  lastSync }

        return query.map { it.toDomain() }.toList()
    }

    override suspend fun create(data: TCreate, userId: Long): T = safeQuery("Не удалось создать заметку", logger) {
        val baseItem = itemRepository.create(data.base, userId)
        insertSubDetails(baseItem.id, data)
        getFullItem(baseItem.id, userId)
    }

    override suspend fun update(data: TUpdate, userId: Long): T = safeQuery("Не удалось обновить заметку", logger) {
        val baseUpdated = itemRepository.update(data.base, userId)
        updateSubDetails(baseUpdated.id, data)
        getFullItem(baseUpdated.id, userId)
    }

}