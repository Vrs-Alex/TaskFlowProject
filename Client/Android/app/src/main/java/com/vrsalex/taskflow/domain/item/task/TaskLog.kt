package com.vrsalex.taskflow.domain.item.task

import com.vrsalex.taskflow.domain.sync.models.SyncModel
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class TaskLog(
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,
    override val isDeleted: Boolean,

    val taskId: Uuid,
    val date: LocalDate
): SyncModel

data class TaskLogCreate(
    val id: Uuid = Uuid.random(),
    val taskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant = Clock.System.now()
)
