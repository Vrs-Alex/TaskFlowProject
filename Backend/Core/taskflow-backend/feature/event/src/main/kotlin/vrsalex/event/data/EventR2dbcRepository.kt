package vrsalex.event.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.EventTable
import vrsalex.core.exception.AppException
import vrsalex.core.model.isAnyDefined
import vrsalex.event.domain.Event
import vrsalex.event.domain.EventCreate
import vrsalex.event.domain.EventRepository
import vrsalex.event.domain.EventUpdate
import vrsalex.item.data.toItem
import vrsalex.item.domain.repository.BaseSubItemRepository
import vrsalex.item.domain.repository.ItemRepository
import kotlin.uuid.Uuid

class EventR2dbcRepository(
    itemRepository: ItemRepository
): EventRepository, BaseSubItemRepository<Event, EventCreate, EventUpdate>(
    itemRepository,
    EventTable
) {

    override suspend fun getFullItem(id: Long, ownerId: Long): Event =
        findById(id, ownerId) ?: throw AppException.NotFound("Мероприятие не найдено")

    override suspend fun insertSubDetails(itemId: Long, data: EventCreate) {
        EventTable.insert {
            it[EventTable.id] = itemId
            it[startDate] = data.startDate
            it[endDate] = data.endDate
            it[isAllDay] = data.isAllDay
            it[location] = data.location
        }
    }

    override suspend fun updateSubDetails(itemId: Long, data: EventUpdate) {
        if (!isAnyDefined(data.startDate, data.endDate, data.location, data.isAllDay)) return

        EventTable.update(
            where = { EventTable.id eq itemId }
        ){ statement ->
            data.startDate.onDefined { statement[EventTable.startDate] = it }
            data.endDate.onDefined { statement[EventTable.endDate] = it }
            data.isAllDay.onDefined { statement[EventTable.isAllDay] = it }
            data.location.onDefined { statement[EventTable.location] = it }
        }
    }

    override suspend fun ResultRow.toDomain(tagsByItemId: Map<Long, List<Uuid>>): Event = Event(
        base = this.toItem(tagsByItemId),
        startDate = this[EventTable.startDate],
        endDate = this[EventTable.endDate],
        isAllDay = this[EventTable.isAllDay],
        location = this[EventTable.location]
    )

}