package vrsalex.feature.workspace.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.entity.AreaTable
import vrsalex.core.database.repository.BaseSyncRepository
import vrsalex.core.database.utils.exists
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import vrsalex.feature.workspace.domain.repository.AreaRepository
import kotlin.time.Clock

class AreaR2dbcRepository : BaseSyncRepository<Area, AreaTable, AreaCreate, AreaUpdate>(AreaTable), AreaRepository {

    override fun ResultRow.toDomain() = Area(
        id = this[table.id].value,
        ownerId = this[table.ownerId].value,
        name = this[table.name],
        color = this[table.color],
        clientId = this[table.clientId],
        updatedAt = this[table.updatedAt],
        isDeleted = this[table.isDeleted],
        version = this[table.version],
        createdAt = this[table.createdAt]
    )


    override suspend fun create(data: AreaCreate, ownerId: Long): Long {
        return table.insertAndGetId {
            it[AreaTable.ownerId] = ownerId
            it[AreaTable.name] = data.name
            it[AreaTable.color] = data.color
            it[AreaTable.clientId] = data.clientId
            it[AreaTable.version] = 1
        }.value
    }

    override suspend fun update(data: AreaUpdate, ownerId: Long): Boolean = table.update(
            {
                (table.id eq data.id) and (table.ownerId eq ownerId) and (table.version eq data.version)
            }
        ) { statement ->
            data.name?.let { statement[AreaTable.name] = it }
            data.color?.let { statement[AreaTable.color] = it }
            statement[AreaTable.version] = data.version + 1
            statement[AreaTable.updatedAt] = Clock.System.now()
        } > 0


    override suspend fun existByOwnerIdAndName(ownerId: Long, name: String): Boolean =
        table.exists {
            (table.ownerId eq  ownerId) and (table.name eq name)
        }
}