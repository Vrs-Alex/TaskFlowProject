package com.vrsalex.taskflow.data.note.task

import com.vrsalex.network.public.api.item.TaskApi
import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import com.vrsalex.taskflow.domain.note.task.Task
import com.vrsalex.taskflow.domain.note.task.TaskCreate
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.note.task.TaskUpdate
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskRepositoryImpl(
    private val local: TaskLocalDataSource,
    private val logs: TaskLogLocalDataSource,
    private val syncPuller: SyncPuller,
    private val taskApi: TaskApi
) : TaskRepository {

    override fun observeAll(): Flow<List<Task>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Task?> =
        local.observe(id).map { it?.toDomain() }

    override fun observeInbox(): Flow<List<Task>> =
        local.observeInbox().map { list -> list.map { it.toDomain() } }

    override fun observeByDate(date: Instant): Flow<List<Task>> {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return local.observeByDate(localDate).map { list ->
            list.filter { rel -> rel.task.recurrenceType == null || localDate.matchesRecurrence(rel.task) }
                .map { it.toDomain(forDate = localDate) }
        }
    }

    override fun observeByDateRange(from: LocalDate, to: LocalDate): Flow<Map<LocalDate, List<Task>>> =
        local.observeInRange(from, to).map { list ->
            buildMap<LocalDate, MutableList<Task>> {
                list.forEach { rel ->
                    val due = rel.task.dueDate ?: return@forEach
                    if (rel.task.recurrenceType == null) {
                        if (due in from..to) getOrPut(due) { mutableListOf() }.add(rel.toDomain())
                    } else {
                        var date = maxOf(from, due)
                        val end = rel.task.recurrenceEndDate?.let { minOf(it, to) } ?: to
                        while (date <= end) {
                            if (date.matchesRecurrence(rel.task)) {
                                getOrPut(date) { mutableListOf() }.add(rel.toDomain(forDate = date))
                            }
                            date = date.plus(1, DateTimeUnit.DAY)
                        }
                    }
                }
            }
        }

    override fun observeOverdue(today: LocalDate): Flow<List<Task>> =
        local.observeOverdueCandidates(today).map { list ->
            list.mapNotNull { rel ->
                if (rel.task.recurrenceType == null) {
                    rel.toDomain()
                } else {
                    val overdue = latestOccurrenceBefore(today, rel.task) ?: return@mapNotNull null
                    val done = rel.logs.any { !it.sync.isDeleted && it.date == overdue && it.completedAt != null }
                    if (!done) rel.toDomain(forDate = overdue) else null
                }
            }
        }

    override suspend fun create(data: TaskCreate) = local.create(data)
    override suspend fun update(data: TaskUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun setDone(taskId: Uuid, date: LocalDate, done: Boolean) {
        logs.setDone(taskId, date, done)
    }

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncPuller.sync(
            syncEntity = SyncEntity.AREA,
            lastSync = lastSync,
            fetch = { since -> taskApi.sync(since) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { id -> local.delete(id) },
            getLocalSyncModelColumns = { id -> local.getRaw(id) }
        )

    override suspend fun syncById(id: Uuid): Resource<Unit> =
        syncPuller.syncItem(
            id = id,
            fetchItem = { taskApi.syncItem(it) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { local.delete(it) },
        )

    private fun LocalDate.matchesRecurrence(task: TaskEntity): Boolean {
        val start = task.dueDate ?: return false
        if (this < start) return false
        task.recurrenceEndDate?.let { if (this > it) return false }
        return when (task.recurrenceType) {
            RecurrenceType.DAILY -> true
            RecurrenceType.WEEKLY -> {
                val days = task.recurrenceDays?.toInt()
                if (days != null && days != 0) {
                    (days and (1 shl (dayOfWeek.isoDayNumber - 1))) != 0
                } else {
                    dayOfWeek == start.dayOfWeek
                }
            }
            RecurrenceType.MONTHLY -> dayOfMonth == start.dayOfMonth
            RecurrenceType.YEARLY -> dayOfMonth == start.dayOfMonth && month == start.month
            null -> this == start
        }
    }

    private fun latestOccurrenceBefore(today: LocalDate, task: TaskEntity): LocalDate? {
        val start = task.dueDate ?: return null
        val yesterday = today.minus(1, DateTimeUnit.DAY)
        var date = task.recurrenceEndDate?.let { minOf(it, yesterday) } ?: yesterday
        val floor = maxOf(start, today.minus(366, DateTimeUnit.DAY))
        while (date >= floor) {
            if (date.matchesRecurrence(task)) return date
            date = date.minus(1, DateTimeUnit.DAY)
        }
        return null
    }
}
