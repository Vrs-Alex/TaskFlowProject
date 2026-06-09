package com.vrsalex.taskflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vrsalex.taskflow.data.local.db.dao.item.EventDao
import com.vrsalex.taskflow.data.local.db.dao.item.ItemDao
import com.vrsalex.taskflow.data.local.db.dao.item.ItemTagDao
import com.vrsalex.taskflow.data.local.db.dao.item.TaskDao
import com.vrsalex.taskflow.data.local.db.dao.item.TaskLogDao
import com.vrsalex.taskflow.data.local.db.dao.sync.PendingOperationDao
import com.vrsalex.taskflow.data.local.db.dao.sync.SyncDao
import com.vrsalex.taskflow.data.local.db.dao.workspace.AreaDao
import com.vrsalex.taskflow.data.local.db.dao.workspace.TagDao
import com.vrsalex.taskflow.data.local.db.entity.item.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.item.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity
import com.vrsalex.taskflow.data.local.db.entity.sync.PendingOperationEntity
import com.vrsalex.taskflow.data.local.db.entity.sync.SyncEntity
import com.vrsalex.taskflow.data.local.db.entity.workspace.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.workspace.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.workspace.TagEntity

@Database(
    entities = [
        SyncEntity::class,
        PendingOperationEntity::class,

        AreaEntity::class,
        TagEntity::class,

        ItemTagCrossRef::class,

        ItemEntity::class,
        EventEntity::class,
        TaskEntity::class,
        TaskLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun syncDao(): SyncDao
    abstract fun pendingOperationDao(): PendingOperationDao

    abstract fun areaDao(): AreaDao
    abstract fun tagDao(): TagDao
    abstract fun itemTagDao(): ItemTagDao

    abstract fun itemDao(): ItemDao
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun taskLogDao(): TaskLogDao

    suspend fun clearAll() {
        this.clearAllTables()
    }

}