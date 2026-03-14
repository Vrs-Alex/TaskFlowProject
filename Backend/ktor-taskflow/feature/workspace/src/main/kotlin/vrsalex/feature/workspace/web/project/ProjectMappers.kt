package vrsalex.feature.workspace.web.project

import vrsalex.api.dto.workspace.project.ProjectCreateRequest
import vrsalex.api.dto.workspace.project.ProjectDto
import vrsalex.api.dto.workspace.project.ProjectUpdateRequest
import vrsalex.core.model.toModelOptional
import vrsalex.core.value_object.Color
import vrsalex.feature.workspace.domain.model.Project
import vrsalex.feature.workspace.domain.model.ProjectCreate
import vrsalex.feature.workspace.domain.model.ProjectUpdate

fun ProjectCreateRequest.toDomain() = ProjectCreate(
    clientId = this.clientId,
    areaId = this.areaId,
    statusId = this.statusId,
    name = this.name,
    color = Color(this.color),
    description = this.description,
    dueDate = this.dueDate
)


fun ProjectUpdateRequest.toDomain() = ProjectUpdate(
    clientId = this.clientId,
    id = this.id,
    version = this.version,
    areaId = this.areaId.toModelOptional(),
    statusId = this.statusId.toModelOptional(),
    name = this.name.toModelOptional(),
    color = this.color.toModelOptional().map { Color(it) },
    description = this.description.toModelOptional(),
    dueDate = this.dueDate.toModelOptional()
)


fun Project.toDto() = ProjectDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    areaId = this.areaId,
    statusId = this.statusId,
    name = this.name,
    color = this.color.value,
    description = this.description,
    dueDate = this.dueDate,
    createdAt = this.createdAt
)