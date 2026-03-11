package vrsalex.feature.workspace.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import org.flywaydb.core.internal.database.base.Database
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.andWhere
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.entity.AreaTable
import vrsalex.core.database.utils.findOne
import vrsalex.feature.workspace.data.mapper.toArea
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import kotlin.time.Instant
import kotlin.uuid.Uuid

class AreaR2dbcRepository {

    suspend fun findById(id: Long): Area? =
        AreaTable.selectAll()
            .where { AreaTable.id eq id }
            .singleOrNull()?.toArea()

    suspend fun findByClientId(clientId: Uuid): Area? =
        AreaTable.selectAll()
            .where { AreaTable.clientId eq clientId }
            .singleOrNull()?.toArea()

    suspend fun findByUserId(userId: Long): Flow<Area> =
        AreaTable
            .selectAll()
            .where { AreaTable.owner eq userId }
            .map { it.toArea() }



    suspend fun findChangesAfter(ownerId: Long, lastSync: Instant?): Flow<Area> {
        val query = AreaTable.selectAll().where { AreaTable.owner eq ownerId }

        if (lastSync != null) {
            query.andWhere { AreaTable.updatedAt greater lastSync }
        }

        return query.map { it.toArea() }
    }


    suspend fun create(data: AreaCreate): Long {
        return AreaTable.insertAndGetId {
            it[owner] = data.ownerId
            it[name] = data.name
            it[color] = data.color
            it[clientId] = data.clientId
            it[version] = 1
        }.value
    }

    suspend fun update(data: AreaUpdate): Boolean {
        return AreaTable.update({
            (AreaTable.id eq data.id) and (AreaTable.version eq data.version)
        }) {
            it[name] = data.name
            it[color] = data.color
            it[version] = data.version + 1
        } > 0
    }



}