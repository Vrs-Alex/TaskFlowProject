package com.vrsalex.taskflow.data.local.db.dao.item

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.item.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@Dao
interface TaskDao {

    // Search

    @Transaction
    @Query("SELECT item.* FROM item INNER JOIN task ON item.id = task.itemId WHERE item.id = :id")
    suspend fun getTaskByIdRaw(id: Uuid): TaskRelation?

    @Transaction
    @Query("SELECT item.* FROM item INNER JOIN task ON item.id = task.itemId WHERE item.id = :id AND isDeleted = 0")
    fun getTask(id: Uuid): Flow<TaskRelation?>

    @Transaction
    @Query("""
        SELECT item.* FROM item 
        INNER JOIN task ON item.id = task.itemId
        WHERE isDeleted = 0
    """)
    fun getTasks(): Flow<List<TaskRelation>>

    @Transaction
    @Query("""
        SELECT item.* FROM item
        INNER JOIN task ON item.id = task.itemId
        WHERE isDeleted = 0 AND (
            task.dueDate = :date
            OR (task.recurrenceType IS NOT NULL AND task.dueDate <= :date)
        )
        ORDER BY task.dueTime ASC
    """)
    fun getTasks(date: LocalDate): Flow<List<TaskRelation>>



    // Insert, Update, Delete

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    suspend fun upsert(task: TaskEntity) {
        if (insert(task) == -1L) update(task)
    }

    @Query("DELETE FROM task WHERE itemId = :itemId")
    suspend fun delete(itemId: Uuid)


}
