package dto.workspace.tag

import dto.common.SyncDtoEntity
import kotlinx.serialization.Serializable
import serializer.InstantSerializer
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
