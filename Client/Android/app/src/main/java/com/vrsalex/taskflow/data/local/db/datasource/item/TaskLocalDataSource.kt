package com.vrsalex.taskflow.data.local.db.datasource.item

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.item.TaskEntity
import com.vrsalex.taskflow.data.local.db.relation.ItemWithRelations
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

class TaskLocalDataSource(
    private val db: AppDatabase,
    private val itemLocalDataSource: ItemLocalDataSource
) {

    suspend fun getTaskByIdRaw(id: Uuid): TaskRelation? = db.taskDao().getTaskByIdRaw(id)

    fun getTask(id: Uuid): Flow<TaskRelation?> = db.taskDao().getTask(id)

    fun getTasks(): Flow<List<TaskRelation>> = db.taskDao().getTasks()

    fun getTasksByDate(date: LocalDate): Flow<List<TaskRelation>> =
        db.taskDao().getTasks(date)

    fun getTasksInRange(from: LocalDate, to: LocalDate): Flow<List<TaskRelation>> =
        db.taskDao().getTasksInRange(from, to)

    fun getOverdueCandidates(today: LocalDate): Flow<List<TaskRelation>> =
        db.taskDao().getOverdueCandidates(today)


    suspend fun insert(item: ItemWithRelations, task: TaskEntity) {
        db.withTransaction {
            itemLocalDataSource.insert(item)
            db.taskDao().upsert(task)
        }
    }

    suspend fun update(data: TaskUpdate) {
        db.withTransaction {
            val current = db.taskDao().getTaskByIdRaw(data.base.id) ?: return@withTransaction

            itemLocalDataSource.update(data.base)

            var updatedTask = current.task
            data.dueDate.onDefined { updatedTask = updatedTask.copy(dueDate = it) }
            data.dueTime.onDefined { updatedTask = updatedTask.copy(dueTime = it) }
            data.recurrence.onDefined { recurrence ->
                updatedTask = updatedTask.copy(
                    recurrenceType = recurrence?.type,
                    recurrenceInterval = recurrence?.interval ?: 1,
                    recurrenceDays = recurrence?.days,
                    recurrenceEndDate = recurrence?.endDate,
                    recurrenceCount = recurrence?.count
                )
            }
            db.taskDao().update(updatedTask)
        }
    }
}
