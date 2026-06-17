package com.vrsalex.taskflow.data.note.task

import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import com.vrsalex.taskflow.data.note.toNote
import com.vrsalex.taskflow.domain.note.task.Task
import com.vrsalex.taskflow.domain.note.task.TaskCreate
import com.vrsalex.taskflow.domain.note.task.TaskLog
import com.vrsalex.taskflow.domain.note.task.TaskLogCreate
import kotlinx.datetime.LocalDate

fun TaskRelation.toDomain(forDate: LocalDate? = task.dueDate): Task = Task(
    dueDate = task.dueDate,
    dueTime = task.dueTime,
    recurrenceType = task.recurrenceType,
    recurrenceDays = task.recurrenceDays,
    recurrenceEndDate = task.recurrenceEndDate,
    recurrenceCount = task.recurrenceCount,
    recurrenceInterval = task.recurrenceInterval,
    isCompleted = logs.any { !it.sync.isDeleted && (it.date == forDate || forDate == null) && it.completedAt != null },
    note = note.toNote(area, tags),
)

fun TaskCreate.toTaskEntity(): TaskEntity = TaskEntity(
    id = note.syncModelCreate.id,
    dueDate = dueDate,
    dueTime = dueTime,
    recurrenceType = recurrenceType,
    recurrenceDays = recurrenceDays,
    recurrenceEndDate = recurrenceEndDate,
    recurrenceCount = recurrenceCount,
    recurrenceInterval = recurrenceInterval,
)

fun TaskLogEntity.toDomain(): TaskLog = TaskLog(
    taskId = taskId,
    date = date,
    completedAt = completedAt,
    syncModel = sync.toSyncModel(id),
)

fun TaskLogCreate.toEntity(): TaskLogEntity = TaskLogEntity(
    id = syncModelCreate.id,
    taskId = taskId,
    date = date,
    completedAt = completedAt,
    sync = newLocalSync(),
)
