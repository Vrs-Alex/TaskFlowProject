package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.sync.repository.SyncRepository

interface TaskLogRepository : SyncRepository<TaskLog, TaskLogCreate, TaskLogUpdate>