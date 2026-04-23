package vrsalex.shared.api.area

import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.common.SyncCreateDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.common.SyncUpdateDto
import kotlin.time.Instant
import kotlin.uuid.Uuid


@Serializable
data class AreaDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val createdAt: Instant,

    val name: String,
    val color: String
): SyncDto

@Serializable
data class AreaCreateRequest(
    override val clientId: Uuid,
    val name: String,
    val color: String
): SyncCreateDto

@Serializable
data class AreaUpdateRequest(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,

    val name: OptionalFieldDto<String> = OptionalFieldDto.Undefined,
    val color: OptionalFieldDto<String> = OptionalFieldDto.Undefined
): SyncUpdateDto