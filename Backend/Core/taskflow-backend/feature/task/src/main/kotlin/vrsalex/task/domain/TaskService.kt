package vrsalex.task.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TaskService(
    repository: TaskRepository,
    transactionManager: TransactionManager,
    private val eventPublisher: EventPublisher,
) : BaseSyncService<Task, TaskCreate, TaskUpdate>(repository, transactionManager, eventPublisher) {

    override val entityType: EntityType = EntityType.TASK

    override suspend fun create(data: TaskCreate, userId: Long, userDeviceId: String): Task {
        return super.create(data, userId, userDeviceId).also {
            eventPublisher.publish(AppEvent.PushNotification(userId, userDeviceId, data.base.name, data.base.description ?: ""))
        }
    }

}
