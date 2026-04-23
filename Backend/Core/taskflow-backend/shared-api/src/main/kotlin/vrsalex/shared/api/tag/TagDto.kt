package vrsalex.shared.api.tag

import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.common.SyncCreateDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.common.SyncUpdateDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class TagDto(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    override val updatedAt: Instant,
    override val createdAt: Instant,

    val name: String,
    val color: String
): SyncDto

@Serializable
data class TagCreateRequest(
    override val clientId: Uuid,
    val name: String,
    val color: String
): SyncCreateDto

@Serializable
data class TagUpdateRequest(
    override val id: Long,
    override val clientId: Uuid,
    override val version: Int,
    val name: OptionalFieldDto<String> = OptionalFieldDto.Undefined,
    val color: OptionalFieldDto<String> = OptionalFieldDto.Undefined
): SyncUpdateDto {
    init {
        require(
            (name != OptionalFieldDto.Undefined || color != OptionalFieldDto.Undefined)
        ){
            "Вы не обновили ни одного поля"
        }
    }
}
