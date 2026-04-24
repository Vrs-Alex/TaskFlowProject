package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItem
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface AreaDao {

    @Transaction
    @Query("SELECT * FROM area")
    fun getAreas(): Flow<List<AreaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: AreaEntity)

    @Update
    suspend fun update(event: AreaEntity)

    @Query("DELETE FROM area WHERE id = :areaId")
    suspend fun delete(areaId: Uuid)
}