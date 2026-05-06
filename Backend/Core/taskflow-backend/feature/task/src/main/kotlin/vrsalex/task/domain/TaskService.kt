package vrsalex.task.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TaskService(
    repository: TaskRepository,
    transactionManager: TransactionManager,
    eventBus: EventBus
) : BaseSyncService<Task, TaskCreate, TaskUpdate, TaskRepository>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.TASK

}
