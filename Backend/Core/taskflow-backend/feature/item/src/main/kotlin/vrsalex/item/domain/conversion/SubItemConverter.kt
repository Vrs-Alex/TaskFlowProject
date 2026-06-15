package vrsalex.item.domain.conversion

import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemType
import vrsalex.shared.api.item.conversion.ConvertItemRequest

/**
 * SPI для конвертации типов item.
 *
 * Каждый под-тип (task, event, ...) реализует этот интерфейс в своём модуле и регистрирует
 * его в Koin. [ItemConversionService] собирает все реализации через `getAll()` и не знает
 * про конкретные модули — благодаря этому `feature/item` остаётся базовым и не зависит от
 * `feature/task` / `feature/event`.
 *
 * Конвертер — это адаптер: он маппит транспортный [ConvertItemRequest] в доменную модель
 * своего типа и пишет extension-строку. NOTE реализации не имеет — у заметки нет extension.
 */
interface SubItemConverter {

    val type: ItemType

    /** Создаёт extension-строку своего типа для уже существующего [item]. */
    suspend fun insertDetails(item: Item, request: ConvertItemRequest)

    /** Снимает extension-строку этого типа (вместе с зависимыми данными, напр. task_log по каскаду). */
    suspend fun deleteDetails(itemId: Long)
}
