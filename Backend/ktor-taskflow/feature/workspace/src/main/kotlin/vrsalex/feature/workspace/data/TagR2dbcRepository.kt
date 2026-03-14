package vrsalex.feature.workspace.data


import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.database.core.TagTable
import vrsalex.core.database.repository.BaseSyncRepository
import vrsalex.core.database.utils.exists
import vrsalex.core.exception.AppException
import vrsalex.core.value_object.Color
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate
import vrsalex.feature.workspace.domain.repository.TagRepository
import kotlin.time.Clock

class TagR2dbcRepository: BaseSyncRepository<Tag, TagTable, TagCreate, TagUpdate>(TagTable), TagRepository {

    override fun ResultRow.toDomain(): Tag =
        Tag(
            id = this[TagTable.id].value,
            ownerId = this[TagTable.ownerId].value,
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
        ownerId: Long
    ): Long {
        return try {
            TagTable.insertAndGetId {
                it[TagTable.ownerId] = ownerId
                it[TagTable.clientId] = data.clientId
                it[TagTable.name] = data.name
                it[TagTable.color] = data.color.value
            }.value
        } catch (e: Exception) {
            throw AppException.BadRequest("Тег не создан. Вы отправили неверные данные.")
        }
    }

    override suspend fun update(
        data: TagUpdate,
        ownerId: Long
    ): Tag? {
        try {
            val updatedRows = TagTable.update(
                {
                    (table.id eq data.id) and (table.ownerId eq ownerId) and (table.version eq data.version)
                },
            ) { statement ->
                data.name.onDefined { statement[TagTable.name] = it }
                data.color.onDefined { statement[TagTable.color] = it.value }
                statement[TagTable.version] = data.version + 1
                statement[TagTable.updatedAt] = Clock.System.now()
            }
            if (updatedRows == 0) return null
            return findById(data.id, ownerId)
        } catch (e: Exception) {
            throw AppException.BadRequest("Тег не обновлен. Вы отправили неверные данные.")
        }
    }

    override suspend fun existByOwnerIdAndName(ownerId: Long, name: String): Boolean =
    table.exists {
        (table.ownerId eq  ownerId) and (table.name eq name)
    }
}