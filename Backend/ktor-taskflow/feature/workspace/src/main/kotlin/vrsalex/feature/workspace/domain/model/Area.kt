package vrsalex.feature.workspace.domain.model

import vrsalex.core.sync.SyncClientId
import vrsalex.core.sync.SyncModel
import vrsalex.core.sync.SyncUpdateModel
import kotlin.time.Instant
import kotlin.uuid.Uuid


data class Area(
    override val id: Long,
    override val ownerId: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val isDeleted: Boolean,

    val name: String,
    val color: String,
    val createdAt: Instant
): SyncModel


data class AreaCreate(
    override val clientId: Uuid,
    val name: String,
    val color: String
): SyncClientId


data class AreaUpdate(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,
    val name: String? = null,
    val color: String? = null
): SyncClientId, SyncUpdateModel