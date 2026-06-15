package com.vrsalex.taskflow.domain.item.task

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.base.SubItemCreate
import com.vrsalex.taskflow.domain.item.base.SubItemUpdate
import com.vrsalex.taskflow.domain.sync.models.SyncId
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.models.SyncUpdateModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


data class Task(
    val base: Item,
    override val isSynced: Boolean,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrence: Recurrence?,
    val completedLogs: List<TaskLog> = emptyList()
) : SyncModel by base

data class TaskCreate(
    override val base: ItemCreate,
    val dueDate: LocalDate?,
    val dueTime: LocalTime? = null,
    val recurrence: Recurrence? = null
) : SubItemCreate, SyncId by base

data class TaskUpdate(
    override val base: ItemUpdate,
    val dueDate: OptionalField<LocalDate?> = OptionalField.Undefined,
    val dueTime: OptionalField<LocalTime?> = OptionalField.Undefined,
    val recurrence: OptionalField<Recurrence?> = OptionalField.Undefined
) : SubItemUpdate, SyncUpdateModel by base
