package vrsalex.item.domain.conversion

import org.slf4j.LoggerFactory
import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.event_bus.domain.AppEvent
import vrsalex.core.event_bus.domain.EventPublisher
import vrsalex.core.exception.AppException
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.toEntityType
import vrsalex.item.domain.repository.ItemRepository
import vrsalex.shared.api.item.conversion.ConvertItemRequest
import kotlin.uuid.Uuid

/**
 * Оркестрирует смену типа item (NOTE <-> TASK <-> EVENT ...).
 *
 * Конвертация — это НЕ update: она пересекает границу типов, поэтому живёт на уровне базового item,
 * а не в сервисе конкретного типа. `id` и `clientId` сущности сохраняются — меняется только `type`,
 * набор extension-строк и версия.
 *
 * Возвращает базовый [Item] (без extension-полей нового типа): клиент догружает детали обычным
 * sync нового типа, дополнительно его подтолкнёт realtime-событие [AppEvent.EntityChanged].
 */
class ItemConversionService(
    private val itemRepository: ItemRepository,
    private val transactionManager: TransactionManager,
    private val eventPublisher: EventPublisher,
    converters: List<SubItemConverter>,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val converters: Map<ItemType, SubItemConverter> = converters.associateBy { it.type }

    suspend fun convert(
        clientId: Uuid,
        request: ConvertItemRequest,
        userId: Long,
        userDeviceId: String,
    ): Item = transactionManager.dbTransaction {
        val item = itemRepository.findByClientId(clientId, userId)
            ?: throw AppException.NotFound("Сущность не найдена")

        if (item.version != request.version) throw AppException.Conflict("Версия устарела")

        val targetType = request.targetType()
        if (item.type == targetType) return@dbTransaction item

        converters[item.type]?.deleteDetails(item.id)

        converters[targetType]?.insertDetails(item, request)

        itemRepository.changeType(item.id, targetType)

        val updated = itemRepository.findByClientId(clientId, userId)!!
        eventPublisher.publish(
            AppEvent.EntityChanged(
                updated.userId, updated.id, targetType.toEntityType(), updated.updatedAt, userDeviceId
            )
        )
        updated
    }
}

private fun ConvertItemRequest.targetType(): ItemType = when (this) {
    is ConvertItemRequest.ToNote -> ItemType.NOTE
    is ConvertItemRequest.ToTask -> ItemType.TASK
    is ConvertItemRequest.ToEvent -> ItemType.EVENT
}
