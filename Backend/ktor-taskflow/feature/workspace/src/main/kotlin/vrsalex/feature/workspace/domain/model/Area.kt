package vrsalex.feature.workspace.domain.model

import vrsalex.core.model.ModelOptionalField
import vrsalex.core.sync.SyncClientId
import vrsalex.core.sync.SyncModel
import vrsalex.core.sync.SyncUpdateModel
import vrsalex.core.value_object.Color
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
    val color: Color,
    val createdAt: Instant
): SyncModel


data class AreaCreate(
    override val clientId: Uuid,
    val name: String,
    val color: Color
): SyncClientId


data class AreaUpdate(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,
    val name: ModelOptionalField<String> = ModelOptionalField.Undefined,
    val color: ModelOptionalField<Color> = ModelOptionalField.Undefined
): SyncUpdateModel