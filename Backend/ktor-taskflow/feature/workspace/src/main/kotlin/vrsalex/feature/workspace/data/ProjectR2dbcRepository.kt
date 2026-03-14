package vrsalex.feature.workspace.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.repository.BaseSyncRepository
import vrsalex.core.database.utils.exists
import vrsalex.core.exception.AppException
import vrsalex.core.sync.SyncRepository
import vrsalex.core.value_object.Color
import vrsalex.database.core.ProjectTable
import vrsalex.feature.workspace.domain.model.Project
import vrsalex.feature.workspace.domain.model.ProjectCreate
import vrsalex.feature.workspace.domain.model.ProjectUpdate
import vrsalex.feature.workspace.domain.repository.ProjectRepository

class ProjectR2dbcRepository: BaseSyncRepository<Project, ProjectTable, ProjectCreate, ProjectUpdate>(ProjectTable),
    ProjectRepository {

    override fun ResultRow.toDomain(): Project =
        Project(
            ownerId = this[table.ownerId].value,
            id = this[table.id].value,
            clientId = this[table.clientId],
            version = this[table.version],
            updatedAt = this[table.updatedAt],
            isDeleted = this[table.isDeleted],
            areaId = this[table.area]?.value,
            statusId = this[table.status].value,
            name = this[table.name],
            color = Color(this[table.color]),
            description = this[table.description],
            dueDate = this[table.dueDate],
            createdAt = this[table.createdAt]
        )

    override suspend fun create(
        data: ProjectCreate,
        ownerId: Long
    ): Long {
        return try {
            ProjectTable.insertAndGetId {
                it[ProjectTable.ownerId] = ownerId
                it[ProjectTable.clientId] = data.clientId
                it[ProjectTable.area] = data.areaId
                it[ProjectTable.status] = data.statusId
                it[ProjectTable.name] = data.name
                it[ProjectTable.color] = data.color.value
                it[ProjectTable.description] = data.description
                it[ProjectTable.dueDate] = data.dueDate
            }.value
        } catch (e: Exception) {
            throw AppException.BadRequest("Проект не создан. Вы прислали неверные данные")
        }
    }

    override suspend fun update(
        data: ProjectUpdate,
        ownerId: Long
    ): Project? {
        try {
            val updatedObject = ProjectTable.update(
                where = {
                    (ProjectTable.id eq data.id) and
                            (ProjectTable.ownerId eq ownerId) and
                            (ProjectTable.version eq data.version)
                }
            ) { statement ->
                data.areaId.onDefined { statement[ProjectTable.area] = it }
                data.statusId.onDefined { statement[ProjectTable.status] = it }
                data.name.onDefined { statement[ProjectTable.name] = it }
                data.color.onDefined { statement[ProjectTable.color] = it.value }
                data.description.onDefined { statement[ProjectTable.description] = it }
                data.dueDate.onDefined { statement[ProjectTable.dueDate] = it }
                statement[ProjectTable.version] = data.version + 1
            }
            if (updatedObject == 0) return null
            return findById(data.id, ownerId)
        } catch (e: Exception) {
            throw AppException.BadRequest("Проект не обновлен. Вы прислали неверные данные")
        }
    }

    override suspend fun existByOwnerIdAndName(ownerId: Long, name: String): Boolean =
        table.exists {
            (table.ownerId eq  ownerId) and (table.name eq name)
        }
}