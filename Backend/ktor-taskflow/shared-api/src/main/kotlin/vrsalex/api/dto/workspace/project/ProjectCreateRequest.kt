package vrsalex.api.dto.workspace.project

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import vrsalex.api.dto.common.SyncCreateEntity

import kotlin.uuid.Uuid

@Serializable
data class ProjectCreateRequest(
    override val clientId: Uuid,
    val areaId: Long?,
    val statusId: Int,
    val name: String,
    val color: String,
    val description: String?,
    val dueDate: LocalDate?
): SyncCreateEntity
