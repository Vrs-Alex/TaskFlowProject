package vrsalex.api.dto.workspace.project

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import vrsalex.api.dto.common.SyncDtoEntity
import vrsalex.api.serializer.InstantSerializer
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ProjectDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    @Serializable(with = InstantSerializer::class)
    override val updatedAt: Instant,
    val areaId: Long?,
    val statusId: Int,
    val name: String,
    val color: String,
    val description: String?,
    val dueDate: LocalDate?,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
): SyncDtoEntity
