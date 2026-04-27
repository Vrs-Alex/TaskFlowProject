package vrsalex.shared.api.item.event

import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.time.Instant

@Serializable
data class EventDto(
    val base: ItemDto,
    val startDate: Instant,
    val endDate: Instant,
    val isAllDay: Boolean,
    val location: String?
) : SyncDto by base

@Serializable
data class EventCreateRequest(
    val base: ItemCreateRequest,
    val startDate: Instant,
    val endDate: Instant,
    val isAllDay: Boolean,
    val location: String? = null
) {
    init {
        require(endDate > startDate) { "Конец события должен быть после начала" }
    }
}

@Serializable
data class EventUpdateRequest(
    val base: ItemUpdateRequest,
    val startDate: OptionalFieldDto<Instant> = OptionalFieldDto.Undefined,
    val endDate: OptionalFieldDto<Instant> = OptionalFieldDto.Undefined,
    val isAllDay: OptionalFieldDto<Boolean> = OptionalFieldDto.Undefined,
    val location: OptionalFieldDto<String?> = OptionalFieldDto.Undefined
){
    init {
        if (startDate is OptionalFieldDto.Defined && endDate is OptionalFieldDto.Defined) {
            require(endDate.value > startDate.value) {
                "Время окончания события должно быть позже времени начала"
            }
        }
    }
}