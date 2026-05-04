package vrsalex.area.data

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
import vrsalex.area.domain.Area
import vrsalex.area.domain.AreaCreate
import vrsalex.area.domain.AreaFilter
import vrsalex.area.domain.AreaRepository
import vrsalex.area.domain.AreaUpdate
import vrsalex.core.database.AreaTable
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.core.value_object.Color
import kotlin.time.Clock

class AreaR2dbcRepository: AreaRepository, BaseSyncRepository<Area, AreaCreate, AreaUpdate, AreaTable>(AreaTable) {

    override suspend fun create(data: AreaCreate, userId: Long): Area =
        safeQuery(
            "Не удалось создать область",
            logger
        ){
            val id = AreaTable.insertAndGetId {
                it[AreaTable.userId] = userId
                it[AreaTable.clientId] = data.clientId
                it[AreaTable.name] = data.name
                it[AreaTable.color] = data.color.value
            }.value

            findById(id, userId) ?: throw AppException.BadRequest("Не удалось создать область")
        }

    override suspend fun update(data: AreaUpdate, userId: Long): Area =
        safeQuery(
            "Не удалось обновить область",
            logger
        ){
            val updatedRows = AreaTable.update(
                {
                    (table.id eq data.id) and (table.clientId eq data.clientId) and
                            (table.userId eq userId) and (table.version eq data.version)
                }
            ) { statement ->
                data.name.onDefined { statement[table.name] = it }
                data.color.onDefined { statement[table.color] = it.value }
                statement[table.version] = data.version + 1
                statement[table.updatedAt] = Clock.System.now()
            }

            if (updatedRows == 0) throw AppException.Conflict("Область не найдена")

            findById(data.id, userId) ?: throw AppException.BadRequest("Не удалось обновить область")
        }

    override suspend fun ResultRow.toDomain(): Area = Area(
        id = this[AreaTable.id].value,
        userId = this[AreaTable.userId].value,
        clientId = this[AreaTable.clientId],
        updatedAt = this[AreaTable.updatedAt],
        version = this[AreaTable.version],
        createdAt = this[AreaTable.createdAt],
        isDeleted = this[AreaTable.isDeleted],
        name = this[AreaTable.name],
        color = Color(this[AreaTable.color]),
    )

    override suspend fun search(filter: AreaFilter, userId: Long): List<Area> {
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