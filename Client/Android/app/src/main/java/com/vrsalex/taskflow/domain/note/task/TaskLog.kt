package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.sync.model.ISyncModel
import com.vrsalex.taskflow.domain.sync.model.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.model.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.model.SyncModel
import com.vrsalex.taskflow.domain.sync.model.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.model.SyncModelUpdate
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class TaskLog(
    val taskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant?,
    override val syncModel: SyncModel
) : ISyncModel

data class TaskLogCreate(
    val taskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant?,
    override val syncModelCreate: SyncModelCreate
) : ISyncModelCreate

data class TaskLogUpdate(override val syncModelUpdate: SyncModelUpdate): ISyncModelUpdate