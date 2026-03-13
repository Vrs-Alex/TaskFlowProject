package vrsalex.api.dto.workspace.tag

import vrsalex.api.dto.common.SyncDtoEntity
import kotlinx.serialization.Serializable
import vrsalex.api.serializer.InstantSerializer
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class TagDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    @Serializable(with = InstantSerializer::class)
    override val updatedAt: Instant,

    val name: String,
    val color: String
): SyncDtoEntity
