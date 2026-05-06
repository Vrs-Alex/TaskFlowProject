package vrsalex.task.domain

import kotlinx.datetime.LocalDate
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class TaskLog(
    override val id: Long,
    override val userId: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val isDeleted: Boolean,
    override val createdAt: Instant,
    val taskId: Long?,
    val clientTaskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant
) : SyncModel

data class TaskLogCreate(
    override val clientId: Uuid,
    val taskId: Long?,
    val clientTaskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant
) : SyncClientId

data class TaskLogUpdate(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int
) : SyncUpdateModel
