package com.vrsalex.taskflow.data.local.db.dao.sync

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vrsalex.taskflow.data.local.db.entity.sync.PendingOperationEntity
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.uuid.Uuid

@Dao
interface PendingOperationDao {

    // Search

    @Query("SELECT * FROM pending_operation WHERE id = :itemId LIMIT 1")
    suspend fun findByItemId(itemId: Uuid): PendingOperationEntity?

    @Query("SELECT * FROM pending_operation WHERE entityType = :entityType")
    suspend fun getByEntityType(entityType: SyncDbEntity): List<PendingOperationEntity>

    @Query("SELECT * FROM pending_operation ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingOperationEntity>

    // Insert, Delete

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operation: PendingOperationEntity)

    @Query("DELETE FROM pending_operation WHERE id = :itemId")
    suspend fun delete(itemId: Uuid)

    @Query("UPDATE pending_operation SET retryCount = retryCount + 1 WHERE id = :itemId")
    suspend fun incrementRetryCount(itemId: Uuid)

}