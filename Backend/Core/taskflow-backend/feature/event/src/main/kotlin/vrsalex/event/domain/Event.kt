package vrsalex.event.domain

import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.model.SubItemCreate
import vrsalex.item.domain.model.SubItemUpdate
import kotlin.time.Instant

data class Event(
    val base: Item,
    val startDate: Instant,
    val endDate: Instant,
    val isAllDay: Boolean,
    val location: String?
): SyncModel by base


data class EventCreate(
    override val base: ItemCreate,
    val startDate: Instant,
    val endDate: Instant,
    val isAllDay: Boolean,
    val location: String?
): SubItemCreate, SyncClientId by base


data class EventUpdate(
    override val base: ItemUpdate,
    val startDate: OptionalField<Instant> = OptionalField.Undefined,
    val endDate: OptionalField<Instant> = OptionalField.Undefined,
    val isAllDay: OptionalField<Boolean> = OptionalField.Undefined,
    val location: OptionalField<String?> = OptionalField.Undefined
): SubItemUpdate, SyncUpdateModel by base
