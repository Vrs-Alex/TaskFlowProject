package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.sync.SyncRepository

interface TaskLogRepository : SyncRepository<TaskLog, TaskLogCreate, TaskLogUpdate>