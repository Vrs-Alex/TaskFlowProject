package vrsalex.feature.workspace.data.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.core.database.entity.AreaTable
import vrsalex.feature.workspace.domain.model.Area

fun ResultRow.toArea() = Area(
    id = this[AreaTable.id].value,
    userId = this[AreaTable.owner].value,
    name = this[AreaTable.name],
    color = this[AreaTable.color],
    clientId = this[AreaTable.clientId],
    createdAt = this[AreaTable.createdAt],
    updatedAt = this[AreaTable.updatedAt],
    isDeleted = this[AreaTable.isDeleted],
    version = this[AreaTable.version]
)