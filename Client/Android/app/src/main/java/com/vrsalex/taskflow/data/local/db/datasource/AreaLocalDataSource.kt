package com.vrsalex.taskflow.data.local.db.datasource

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.domain.workscape.area.AreaUpdate
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class AreaLocalDataSource(private val db: AppDatabase) {

    fun getAreas(): Flow<List<AreaEntity>> = db.areaDao().getAreas()
    
    fun getArea(id: Uuid): Flow<AreaEntity?> = db.areaDao().getArea(id)

    suspend fun getByIdRaw(id: Uuid): AreaEntity? = db.areaDao().getByIdRaw(id)

    suspend fun insert(area: AreaEntity) = db.areaDao().upsert(area)

    suspend fun update(data: AreaUpdate) {
        db.withTransaction {
            val current = db.areaDao().getByIdRaw(data.id) ?: return@withTransaction
            
            var updated = current.copy(isSynced = false)
            data.name.onDefined { updated = updated.copy(name = it) }
            data.color.onDefined { updated = updated.copy(color = it) }
            
            db.areaDao().update(updated)
        }
    }

    suspend fun delete(id: Uuid) = db.areaDao().delete(id)
    
    suspend fun softDelete(id: Uuid) = db.areaDao().softDelete(id)
}