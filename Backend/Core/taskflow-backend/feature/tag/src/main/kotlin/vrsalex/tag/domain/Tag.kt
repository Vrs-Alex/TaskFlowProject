package vrsalex.tag.domain

import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.core.value_object.Color
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Tag(
    override val id: Long,
    override val userId: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val isDeleted: Boolean,
    override val createdAt: Instant,

    val name: String,
    val color: Color
): SyncModel



data class TagCreate(
    override val clientId: Uuid,
    val name: String,
    val color: Color
): SyncClientId


data class TagUpdate(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    val name: OptionalField<String> = OptionalField.Undefined,
    val color: OptionalField<Color> = OptionalField.Undefined
): SyncClientId, SyncUpdateModel