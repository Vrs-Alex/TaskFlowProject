package vrsalex.tag.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.TagTable
import vrsalex.core.database.utils.exists
import vrsalex.core.database.utils.findOne
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.core.value_object.Color
import vrsalex.tag.domain.Tag
import vrsalex.tag.domain.TagCreate
import vrsalex.tag.domain.TagFilter
import vrsalex.tag.domain.TagRepository
import vrsalex.tag.domain.TagUpdate
import kotlin.time.Clock

class TagR2dbcRepository: TagRepository, BaseSyncRepository<Tag, TagCreate, TagUpdate, TagTable>(TagTable) {

    override suspend fun ResultRow.toDomain(): Tag =
        Tag(
            id = this[TagTable.id].value,
            userId = this[TagTable.userId].value,
            clientId = this[TagTable.clientId],
            version = this[TagTable.version],
            updatedAt = this[TagTable.updatedAt],
            isDeleted = this[TagTable.isDeleted],
            name = this[TagTable.name],
            color = Color(this[TagTable.color]),
            createdAt = this[TagTable.createdAt]
        )

    override suspend fun create(
        data: TagCreate,
        userId: Long
    ): Tag = safeQuery(
        "Не удалось создать тег",
        logger
    ) {
        val id = TagTable.insertAndGetId {
            it[TagTable.userId] = userId
            it[TagTable.clientId] = data.clientId
            it[TagTable.name] = data.name
            it[TagTable.color] = data.color.value
        }.value
        findById(id, userId) ?: throw AppException.BadRequest("Не удалось создать тег")
    }

    override suspend fun update(
        data: TagUpdate,
        userId: Long
    ): Tag  = safeQuery(
        "Не удалось обновить тег",
        logger
    ) {
        val updatedRows = TagTable.update(
            {
                (table.id eq data.id) and (table.clientId eq data.clientId) and
                        (table.userId eq userId) and (table.version eq data.version)
            },
        ) { statement ->
            data.name.onDefined { statement[TagTable.name] = it }
            data.color.onDefined { statement[TagTable.color] = it.value }
            statement[TagTable.version] = data.version + 1
            statement[TagTable.updatedAt] = Clock.System.now()
        }
        checkUpdateResult(updatedRows, data.id, userId, "Тег")
        findById(data.id, userId) ?: throw AppException.BadRequest("Не удалось обновить тег")
    }

    override suspend fun existByUserIdAndName(userId: Long, name: String): Boolean = table.exists {
        (table.userId eq  userId) and (table.name eq name) and (table.isDeleted eq false)
    }

    override suspend fun search(filter: TagFilter, userId: Long): List<Tag> {
        return table.selectAll()
            .where { table.userId eq userId }
            .apply {
                filter.name?.let {
                    if (filter.nameExact) andWhere { table.name eq it }
                    else andWhere { table.name like "%$it%" }
                }
            }
            .apply { filter.color?.let { andWhere { table.color eq it.value } } }
            .map { it.toDomain() }
            .toList()
    }


}