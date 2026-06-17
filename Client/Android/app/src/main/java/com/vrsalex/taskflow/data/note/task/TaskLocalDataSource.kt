package com.vrsalex.taskflow.data.note.task

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import com.vrsalex.taskflow.data.note.applyTo
import com.vrsalex.taskflow.data.note.tagIds
import com.vrsalex.taskflow.data.note.tagIdsOrNull
import com.vrsalex.taskflow.data.note.toEntity
import com.vrsalex.taskflow.domain.note.task.TaskCreate
import com.vrsalex.taskflow.domain.note.task.TaskUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import vrsalex.shared.api.item.task.TaskDto
import kotlin.uuid.Uuid

class TaskLocalDataSource(private val db: AppDatabase) {

    private val noteDao get() = db.noteDao()
    private val taskDao get() = db.taskDao()

    fun observeAll(): Flow<List<TaskRelation>> = taskDao.observeAll()
    fun observe(id: Uuid): Flow<TaskRelation?> = taskDao.observe(id)
    fun observeInbox(): Flow<List<TaskRelation>> = taskDao.observeInbox()
    fun observeByDate(date: LocalDate): Flow<List<TaskRelation>> = taskDao.observeByDate(date)
    fun observeInRange(from: LocalDate, to: LocalDate): Flow<List<TaskRelation>> = taskDao.observeInRange(from, to)
    fun observeOverdueCandidates(today: LocalDate): Flow<List<TaskRelation>> = taskDao.observeOverdueCandidates(today)
    suspend fun getRaw(id: Uuid) = taskDao.getRaw(id)

    suspend fun create(data: TaskCreate) = db.withTransaction {
        noteDao.upsertWithTags(data.note.toEntity(), data.note.tagIds())
        taskDao.upsert(data.toTaskEntity())
    }

    suspend fun upsertFromRemote(dto: TaskDto) = db.withTransaction {
        noteDao.upsertWithTags(dto.base.toEntity(), dto.base.tags)
        taskDao.upsert(dto.toTaskEntity())
    }

    suspend fun update(data: TaskUpdate) = db.withTransaction {
        val currentNote = noteDao.getRaw(data.note.syncModelUpdate.id) ?: return@withTransaction
        val updatedNote = data.note.applyTo(currentNote)
        val tagIds = data.note.tagIdsOrNull()
        if (tagIds != null) noteDao.upsertWithTags(updatedNote, tagIds) else noteDao.upsert(updatedNote)

        val currentTask = taskDao.getRaw(currentNote.id)?.task ?: return@withTransaction
        var t = currentTask
        data.dueDate.onDefined { t = t.copy(dueDate = it) }
        data.dueTime.onDefined { t = t.copy(dueTime = it) }
        data.recurrenceType.onDefined { t = t.copy(recurrenceType = it) }
        data.recurrenceDays.onDefined { t = t.copy(recurrenceDays = it) }
        data.recurrenceEndDate.onDefined { t = t.copy(recurrenceEndDate = it) }
        data.recurrenceCount.onDefined { t = t.copy(recurrenceCount = it) }
        data.recurrenceInterval.onDefined { t = t.copy(recurrenceInterval = it) }
        taskDao.upsert(t)
    }

    suspend fun softDelete(id: Uuid) = taskDao.softDelete(id)
    suspend fun delete(id: Uuid) = taskDao.delete(id)
}
