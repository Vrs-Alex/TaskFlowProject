package vrsalex.task.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TaskLogService(
    repository: TaskLogRepository,
    transactionManager: TransactionManager,
    eventPublisher: EventPublisher,
) : BaseSyncService<TaskLog, TaskLogCreate, TaskLogUpdate>(repository, transactionManager, eventPublisher) {

    override val entityType: EntityType = EntityType.TASK_LOG

}
