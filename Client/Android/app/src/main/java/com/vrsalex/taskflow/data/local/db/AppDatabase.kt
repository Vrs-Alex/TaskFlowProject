package com.vrsalex.taskflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vrsalex.taskflow.data.local.db.dao.AreaDao
import com.vrsalex.taskflow.data.local.db.dao.EventDao
import com.vrsalex.taskflow.data.local.db.dao.ItemDao
import com.vrsalex.taskflow.data.local.db.dao.ItemTagDao
import com.vrsalex.taskflow.data.local.db.dao.PendingOperationDao
import com.vrsalex.taskflow.data.local.db.dao.SyncDao
import com.vrsalex.taskflow.data.local.db.dao.TagDao
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.PendingOperationEntity
import com.vrsalex.taskflow.data.local.db.entity.SyncEntity
import com.vrsalex.taskflow.data.local.db.entity.TagEntity

@Database(
    entities = [
        SyncEntity::class,
        PendingOperationEntity::class,

        AreaEntity::class,
        TagEntity::class,

        ItemTagCrossRef::class,

        ItemEntity::class,
        EventEntity::class
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

    suspend fun clearAll() {
        this.clearAllTables()
    }

}