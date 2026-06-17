package com.vrsalex.taskflow.data.note.event

import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.event.Event
import com.vrsalex.taskflow.domain.note.event.EventCreate
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.event.EventUpdate
import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.taskflow.presentation.model.note.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.collections.iterator
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class EventRepositoryImpl(
    private val local: EventLocalDataSource,
) : EventRepository {

    override fun observeAll(): Flow<List<Event>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Event?> =
        local.observe(id).map { it?.toDomain() }

    override fun observeByDate(date: Instant): Flow<List<Event>> {
        val tz = TimeZone.currentSystemDefault()
        val day = date.toLocalDateTime(tz).date
        val from = day.atStartOfDayIn(tz)
        val to = day.plus(1, DateTimeUnit.DAY).atStartOfDayIn(tz)
        return local.observeBetween(from, to).map { list -> list.map { it.toDomain() } }
    }

    override fun observeByDateRange(
        start: LocalDate,
        end: LocalDate
    ): Flow<Map<LocalDate, List<Event>>> {
        val tz = TimeZone.currentSystemDefault()
        val from = start.atStartOfDayIn(tz)
        val to = end.atStartOfDayIn(tz).plus(1.days).minus(1.nanoseconds)

        return local.observeBetween(from, to).map { list ->
            buildMap<LocalDate, MutableList<Event>> {
                list.forEach { event ->
                    val event = event.toDomain()
                    val startLocalDate = event.startDate.toLocalDateTime(tz).date
                    val endLocalDate = event.endDate?.toLocalDateTime(tz)?.date ?: startLocalDate
                    var trackingDate = startLocalDate
                    while (trackingDate <= endLocalDate) {
                        getOrPut(trackingDate) { mutableListOf() }.add(event)
                        trackingDate = trackingDate.plus(DatePeriod(days = 1))
                    }
                }
            }
        }
    }


    override fun observeArchived(query: String): Flow<List<Event>> =
        local.observeArchived(query).map { list -> list.map { it.toDomain() } }

    override suspend fun create(data: EventCreate) = local.create(data)
    override suspend fun update(data: EventUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = Resource.Success(Unit)
    override suspend fun syncById(id: Uuid): Resource<Unit> = Resource.Success(Unit)
}
