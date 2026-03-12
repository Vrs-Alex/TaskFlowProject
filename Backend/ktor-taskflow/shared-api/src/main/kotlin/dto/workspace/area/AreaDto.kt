package dto.workspace.area

import dto.common.SyncDtoEntity
import kotlinx.serialization.Serializable
import serializer.InstantSerializer
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class AreaDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    @Serializable(with = InstantSerializer::class)
    override val updatedAt: Instant,

    val name: String,
    val color: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant
) : SyncDtoEntity
