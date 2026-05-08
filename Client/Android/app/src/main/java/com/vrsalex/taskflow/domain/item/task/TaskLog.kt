package com.vrsalex.taskflow.domain.item.task

import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class TaskLog(
    val id: Uuid,
    val serverId: Long?,
    val taskId: Uuid,
    val date: LocalDate
)

data class TaskLogCreate(
    val id: Uuid = Uuid.random(),
    val taskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant = Clock.System.now()
)
