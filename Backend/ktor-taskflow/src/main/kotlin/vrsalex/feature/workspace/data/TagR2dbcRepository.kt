package vrsalex.feature.workspace.data


import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.entity.TagTable
import vrsalex.core.database.repository.BaseSyncRepository
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
            color = this[TagTable.color]
        )

    override suspend fun create(
        data: TagCreate,
        ownerId: Long
    ): Long = TagTable.insertAndGetId {
        it[TagTable.ownerId] = ownerId
        it[TagTable.clientId] = data.clientId
        it[TagTable.name] = data.name
        it[TagTable.color] = data.color
    }.value

    override suspend fun update(
        data: TagUpdate,
        ownerId: Long
    ): Boolean = TagTable.update (
        {
            (table.id eq data.id) and (table.ownerId eq ownerId) and (table.version eq data.version)
        },
    ){ statement ->
        data.name?.let { statement[TagTable.name] = it }
        data.color?.let { statement[TagTable.color] = it }
        statement[TagTable.version] = data.version + 1
        statement[TagTable.updatedAt] = Clock.System.now()
    } > 0
}