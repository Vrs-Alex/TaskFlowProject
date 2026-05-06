package vrsalex.shared.api.item.task

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.SyncCreateDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.common.SyncUpdateDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class TaskLogDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val createdAt: Instant,
    val taskId: Long?,
    val clientTaskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant
) : SyncDto

@Serializable
data class TaskLogCreateRequest(
    override val clientId: Uuid,
    val taskId: Long?,
    val clientTaskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant
) : SyncCreateDto

@Serializable
data class TaskLogUpdateRequest(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int
) : SyncUpdateDto
