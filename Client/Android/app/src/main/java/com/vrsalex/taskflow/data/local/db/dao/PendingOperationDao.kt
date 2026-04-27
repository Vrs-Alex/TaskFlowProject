package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vrsalex.taskflow.data.local.db.entity.PendingOperationEntity
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.uuid.Uuid

@Dao
interface PendingOperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operation: PendingOperationEntity)

    @Query("SELECT * FROM pending_operation ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingOperationEntity>

    @Query("DELETE FROM pending_operation WHERE itemId = :itemId")
    suspend fun delete(itemId: Uuid)

    @Query("SELECT * FROM pending_operation WHERE entityType = :entityType")
    suspend fun getByEntityType(entityType: SyncDbEntity): List<PendingOperationEntity>

}