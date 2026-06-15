package vrsalex.event.domain

import vrsalex.item.domain.conversion.SubItemConverter
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.toItemCreate
import vrsalex.shared.api.item.conversion.ConvertItemRequest

class EventConverter(
    private val repository: EventRepository,
) : SubItemConverter {

    override val type: ItemType = ItemType.EVENT

    override suspend fun insertDetails(item: Item, request: ConvertItemRequest) {
        val data = request as ConvertItemRequest.ToEvent
        repository.insertSubDetails(
            item.id,
            EventCreate(
                base = item.toItemCreate(),
                startDate = data.startDate,
                endDate = data.endDate,
                isAllDay = data.isAllDay,
                location = data.location,
            )
        )
    }

    override suspend fun deleteDetails(itemId: Long) =
        repository.deleteSubDetails(itemId)
}
