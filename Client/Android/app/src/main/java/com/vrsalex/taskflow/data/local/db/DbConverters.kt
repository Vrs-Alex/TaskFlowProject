package com.vrsalex.taskflow.data.local.db

import androidx.room.TypeConverter
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import kotlin.time.Instant
import kotlin.uuid.Uuid

class DbConverters {
    
    @TypeConverter
    fun fromUuid(uuid: Uuid): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUuid(value: String): Uuid {
        return Uuid.parse(value)
    }

    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }


}