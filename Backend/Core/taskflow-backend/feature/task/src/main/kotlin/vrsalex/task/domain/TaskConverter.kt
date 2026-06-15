package vrsalex.task.domain

import vrsalex.item.domain.conversion.SubItemConverter
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.toItemCreate
import vrsalex.shared.api.item.conversion.ConvertItemRequest

class TaskConverter(
    private val repository: TaskRepository,
) : SubItemConverter {

    override val type: ItemType = ItemType.TASK

    override suspend fun insertDetails(item: Item, request: ConvertItemRequest) {
        val data = request as ConvertItemRequest.ToTask
        repository.insertSubDetails(
            item.id,
            TaskCreate(
                base = item.toItemCreate(),
                dueDate = data.dueDate,
                dueTime = data.dueTime,
                recurrence = data.recurrence?.let {
                    Recurrence(
                        type = RecurrenceType.valueOf(it.type.name),
                        interval = it.interval,
                        days = it.days,
                        endDate = it.endDate,
                        count = it.count,
                    )
                },
            )
        )
    }

    override suspend fun deleteDetails(itemId: Long) =
        repository.deleteSubDetails(itemId)
}
