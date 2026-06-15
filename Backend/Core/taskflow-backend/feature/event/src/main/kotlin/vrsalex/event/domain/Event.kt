package vrsalex.event.domain

import vrsalex.core.exception.ensure
import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.item.domain.model.*
import vrsalex.shared.api.exception.ErrorCode
import kotlin.math.E
import kotlin.time.Instant

data class Event(
    val base: Item,
    val startDate: Instant,
    val endDate: Instant?,
    val isAllDay: Boolean,
    val location: String?
): SyncModel by base


data class EventCreate(
    override val base: ItemCreate,
    val startDate: Instant,
    val endDate: Instant?,
    val isAllDay: Boolean,
    val location: String?
): SubItemCreate, SyncClientId by base {
    init {
        ensure(endDate == null || endDate >= startDate, ErrorCode.EVENT_END_BEFORE_START)
        location?.length?.let { ensure(it <= 10_000, ErrorCode.EVENT_LOCATION_TOO_LONG) }
    }
}


data class EventUpdate(
    override val base: ItemUpdate,
    val startDate: OptionalField<Instant> = OptionalField.Undefined,
    val endDate: OptionalField<Instant?> = OptionalField.Undefined,
    val isAllDay: OptionalField<Boolean> = OptionalField.Undefined,
    val location: OptionalField<String?> = OptionalField.Undefined
): SubItemUpdate, SyncUpdateModel by base {
    init {
        startDate.onDefined { startDate ->
            endDate.onDefined { endDate ->
                ensure(endDate == null || endDate >= startDate, ErrorCode.EVENT_END_BEFORE_START)
            }
        }
        location.onDefined { location ->
            location?.length?.let { ensure(it <= 10_000, ErrorCode.EVENT_LOCATION_TOO_LONG) }
        }
    }
}
