package vrsalex.task.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TaskLogService(
    repository: TaskLogRepository,
    transactionManager: TransactionManager,
    eventBus: EventBus
) : BaseSyncService<TaskLog, TaskLogCreate, TaskLogUpdate>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.TASK_LOG

}
