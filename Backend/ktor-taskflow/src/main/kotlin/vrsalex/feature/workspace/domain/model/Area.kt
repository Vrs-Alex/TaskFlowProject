package vrsalex.feature.workspace.domain.model

import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import vrsalex.core.model.SyncEntity
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Area(
    val id: Long,
    val userId: Long,
    val name: String,
    val color: String,

    override val clientId: Uuid,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val isDeleted: Boolean,
    override val version: Int
): SyncEntity


data class AreaCreate(
    val name: String,
    val color: String,
    val clientId: Uuid,
    val ownerId: Long
)


data class AreaUpdate(
    val id: Long,
    val name: String,
    val color: String,
    val version: Int
)