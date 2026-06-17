package com.vrsalex.taskflow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vrsalex.taskflow.data.local.db.converter.RoomConverters
import com.vrsalex.taskflow.data.local.db.dao.AreaDao
import com.vrsalex.taskflow.data.local.db.dao.EventDao
import com.vrsalex.taskflow.data.local.db.dao.NoteDao
import com.vrsalex.taskflow.data.local.db.dao.TagDao
import com.vrsalex.taskflow.data.local.db.dao.TaskDao
import com.vrsalex.taskflow.data.local.db.dao.TaskLogDao
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity

@Database(
    entities = [
        NoteEntity::class,
        TaskEntity::class,
        EventEntity::class,
        TaskLogEntity::class,
        AreaEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun eventDao(): EventDao
    abstract fun taskLogDao(): TaskLogDao
    abstract fun areaDao(): AreaDao
    abstract fun tagDao(): TagDao
}
