package vrsalex.task.domain

import vrsalex.core.sync.repository.SyncRepository

interface TaskLogRepository : SyncRepository<TaskLog, TaskLogCreate, TaskLogUpdate>
