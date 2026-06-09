package vrsalex.task.domain

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.EventBus
import vrsalex.core.event_bus.EventBusData
import vrsalex.core.model.EntityType
import vrsalex.core.sync.service.BaseSyncService

class TaskService(
    repository: TaskRepository,
    transactionManager: TransactionManager,
    private val eventBus: EventBus
) : BaseSyncService<Task, TaskCreate, TaskUpdate>(repository, transactionManager, eventBus) {

    override val entityType: EntityType = EntityType.TASK

    override suspend fun create(data: TaskCreate, userId: Long, userDeviceId: String): Task {
        return super.create(data, userId, userDeviceId).also {
            eventBus.publish(EventBusData.PushNotifications(userId, userDeviceId, data.base.name, data.base.description ?: ""))
        }
    }

}
