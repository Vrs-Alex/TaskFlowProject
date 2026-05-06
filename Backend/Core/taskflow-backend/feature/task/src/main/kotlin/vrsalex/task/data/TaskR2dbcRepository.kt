package vrsalex.task.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.TaskTable
import vrsalex.core.exception.AppException
import vrsalex.core.model.isAnyDefined
import vrsalex.item.data.toItem
import vrsalex.item.domain.repository.BaseSubItemRepository
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.task.domain.Recurrence
import vrsalex.task.domain.RecurrenceType
import vrsalex.task.domain.Task
import vrsalex.task.domain.TaskCreate
import vrsalex.task.domain.TaskRepository
import vrsalex.task.domain.TaskUpdate
import kotlin.uuid.Uuid

class TaskR2dbcRepository(
    itemRepository: ItemRepository
) : TaskRepository, BaseSubItemRepository<Task, TaskCreate, TaskUpdate>(
    itemRepository,
    TaskTable
) {

    override suspend fun getFullItem(id: Long, ownerId: Long): Task =
        findById(id, ownerId) ?: throw AppException.NotFound("Задача не найдена")

    override suspend fun insertSubDetails(itemId: Long, data: TaskCreate) {
        TaskTable.insert {
            it[TaskTable.id] = itemId
            it[dueDate] = data.dueDate
            it[dueTime] = data.dueTime
            it[recurrenceType] = data.recurrence?.type?.name
            it[recurrenceInterval] = data.recurrence?.interval ?: 1
            it[recurrenceDays] = data.recurrence?.days
            it[recurrenceEndDate] = data.recurrence?.endDate
            it[recurrenceCount] = data.recurrence?.count
        }
    }

    override suspend fun updateSubDetails(itemId: Long, data: TaskUpdate) {
        if (!isAnyDefined(data.dueDate, data.dueTime, data.recurrence)) return

        TaskTable.update({ TaskTable.id eq itemId }) { statement ->
            data.dueDate.onDefined { statement[TaskTable.dueDate] = it }
            data.dueTime.onDefined { statement[TaskTable.dueTime] = it }
            data.recurrence.onDefined { rec ->
                statement[TaskTable.recurrenceType] = rec?.type?.name
                statement[TaskTable.recurrenceInterval] = rec?.interval ?: 1
                statement[TaskTable.recurrenceDays] = rec?.days
                statement[TaskTable.recurrenceEndDate] = rec?.endDate
                statement[TaskTable.recurrenceCount] = rec?.count
            }
        }
    }

    override suspend fun ResultRow.toDomain(tagsByItemId: Map<Long, List<Uuid>>): Task {
        val recurrenceTypeName = this[TaskTable.recurrenceType]
        val recurrence = recurrenceTypeName?.let {
            Recurrence(
                type = RecurrenceType.valueOf(it),
                interval = this[TaskTable.recurrenceInterval],
                days = this[TaskTable.recurrenceDays],
                endDate = this[TaskTable.recurrenceEndDate],
                count = this[TaskTable.recurrenceCount]
            )
        }
        return Task(
            base = this.toItem(tagsByItemId),
            dueDate = this[TaskTable.dueDate],
            dueTime = this[TaskTable.dueTime],
            recurrence = recurrence
        )
    }
}
