package com.vrsalex.taskflow.domain.item.event

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.base.SubItemCreate
import com.vrsalex.taskflow.domain.item.base.SubItemUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncId
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.models.SyncUpdateModel
import com.vrsalex.taskflow.presentation.common.extension.formatDateRange
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
    val location: String
): SubItemCreate, SyncId by base


data class EventUpdate(
    override val base: ItemUpdate,
    val startDate: OptionalField<Instant> = OptionalField.Undefined,
    val endDate: OptionalField<Instant> = OptionalField.Undefined,
    val isAllDay: OptionalField<Boolean> = OptionalField.Undefined,
    val location: OptionalField<String?> = OptionalField.Undefined
): SubItemUpdate, SyncUpdateModel by base

