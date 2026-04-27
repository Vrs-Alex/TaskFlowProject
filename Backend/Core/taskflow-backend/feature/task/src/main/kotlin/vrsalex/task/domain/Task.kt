package vrsalex.task.domain

import vrsalex.core.sync.model.SyncModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Task(
    override val userId: Long,
    override val id: Long,
    override val clientId: Uuid,
    override val updatedAt: Instant,
    override val version: Int,
    override val isDeleted: Boolean,
    override val createdAt: Instant,


): SyncModel
