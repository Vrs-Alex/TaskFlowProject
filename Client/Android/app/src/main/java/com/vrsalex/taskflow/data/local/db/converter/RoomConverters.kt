package com.vrsalex.taskflow.data.local.db.converter

import androidx.room.TypeConverter
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

class RoomConverters {

    @TypeConverter fun uuidToString(value: Uuid?): String? = value?.toString()
    @TypeConverter fun stringToUuid(value: String?): Uuid? = value?.let(Uuid::parse)

    @TypeConverter fun syncEntityToString(value: SyncEntity): String = value.name
    @TypeConverter fun stringToSyncEntity(value: String): SyncEntity = SyncEntity.valueOf(value)

    @TypeConverter fun instantToEpochMillis(value: Instant?): Long? = value?.toEpochMilliseconds()
    @TypeConverter fun epochMillisToInstant(value: Long?): Instant? = value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter fun localDateToString(value: LocalDate?): String? = value?.toString()
    @TypeConverter fun stringToLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter fun localTimeToString(value: LocalTime?): String? = value?.toString()
    @TypeConverter fun stringToLocalTime(value: String?): LocalTime? = value?.let(LocalTime::parse)
}
