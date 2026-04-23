package vrsalex.shared.api.item.base

import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.common.SyncCreateDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.common.SyncUpdateDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class ItemDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val createdAt: Instant,
    val name: String,
    val description: String?,
    val status: ItemStatusDto,
    val type: ItemTypeDto,
    val priority: Short,
    val areaId: Long?
): SyncDto


@Serializable
data class ItemCreateRequest(
    override val clientId: Uuid,
    val name: String,
    val description: String?,
    val priority: Short,
    val areaId: Long?,
    val tags: List<Uuid>
): SyncCreateDto


@Serializable
data class ItemUpdateRequest(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,
    val name: OptionalFieldDto<String> = OptionalFieldDto.Undefined,
    val description: OptionalFieldDto<String?> = OptionalFieldDto.Undefined,
    val status: OptionalFieldDto<ItemStatusDto> = OptionalFieldDto.Undefined,
    val priority: OptionalFieldDto<Short> = OptionalFieldDto.Undefined,
    val areaId: OptionalFieldDto<Long?> = OptionalFieldDto.Undefined,
    val tags: OptionalFieldDto<List<Uuid>> = OptionalFieldDto.Undefined
): SyncUpdateDto