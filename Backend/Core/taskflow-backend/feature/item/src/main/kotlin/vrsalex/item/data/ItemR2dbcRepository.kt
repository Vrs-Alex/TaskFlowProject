package vrsalex.item.data

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.ItemTable
import vrsalex.core.database.ItemTagsTable
import vrsalex.core.database.TagTable
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemStatus
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.ItemUpdate
import kotlin.time.Clock
import kotlin.uuid.Uuid

class ItemR2dbcRepository: BaseSyncRepository<Item, ItemCreate, ItemUpdate, ItemTable>(ItemTable), ItemRepository {

    override fun ResultRow.toDomain(): Item = Item(
        userId = this[table.userId].value,
        id = this[table.id].value,
        clientId = this[table.clientId],
        updatedAt = this[table.updatedAt],
        version = this[table.version],
        isDeleted = this[table.isDeleted],
        createdAt = this[table.createdAt],
        name = this[table.name],
        description = this[table.description],
        status = ItemStatus.valueOf(this[table.status]),
        type = ItemType.valueOf(this[table.type]),
        priority = this[table.priority],
        areaId = this[table.areaId]?.value
    )

    override suspend fun create(data: ItemCreate, _userId: Long): Item = safeQuery(
        "Не удалось создать заметку",
        logger
    ) {
        val id = table.insertAndGetId {
            it[userId] = _userId
            it[clientId] = data.clientId
            it[name] = data.name
            it[description] = data.description
            it[status] = ItemStatus.ACTIVE.name
            it[type] = data.type.name
            it[priority] = data.priority
            it[areaId] = data.areaId
        }.value
        if (data.tags.isNotEmpty()) updateTags(id, data.tags)
        findById(id, _userId) ?: throw AppException.BadRequest("Не удалось создать заметку")
    }

    override suspend fun update(data: ItemUpdate, userId: Long): Item = safeQuery(
        "Не удалось обновить заметку",
        logger
    ) {
        val updatedRows = table.update(
            {
                (table.id eq data.id) and (table.clientId eq data.clientId) and
                        (table.userId eq userId) and (table.version eq data.version)
            }
        ) { statement ->
            data.name.onDefined { statement[table.name] = it }
            data.description.onDefined { statement[table.description] = it }
            data.status.onDefined { statement[table.status] = it.name }
            data.priority.onDefined { statement[table.priority] = it }
            data.areaId.onDefined { statement[table.areaId] = it }
            statement[table.version] = data.version + 1
            statement[table.updatedAt] = Clock.System.now()
        }

        if (updatedRows == 0) throw AppException.NotFound("Заметка не найдена")
        data.tags.onDefined { updateTags(data.id, it) }

        findById(data.id, userId) ?: throw AppException.BadRequest("Не удалось обновить заметку")
    }

    override suspend fun updateTags(id: Long, tags: List<Uuid>) = safeQuery(
        "Не удалось обновить теги заметки",
        logger
    ) {
        ItemTagsTable.deleteWhere {
            ItemTagsTable.itemId eq id
        }
        tags.forEach { clientId ->
            val tagId = TagTable
                .selectAll()
                .where { (TagTable.clientId eq clientId) and (TagTable.isDeleted eq false) }
                .singleOrNull()
                ?.get(TagTable.id)
                ?.value
                ?: throw AppException.NotFound("Тег '$clientId' не найден")

            ItemTagsTable.insert {
                it[ItemTagsTable.itemId] = id
                it[ItemTagsTable.tagId] = tagId
            }
        }
    }

    override suspend fun deleteTags(id: Long, tags: List<Uuid>) = safeQuery(
        "Не удалось удалить теги заметки",
        logger
    ) {
        val tagIds = TagTable
            .selectAll()
            .where { TagTable.clientId inList tags }
            .map { it[TagTable.id].value }
            .toList()

        ItemTagsTable.deleteWhere {
            (ItemTagsTable.itemId eq id) and (ItemTagsTable.tagId inList tagIds)
        }
        Unit
    }

}