package vrsalex.shared.api.item.conversion

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vrsalex.shared.api.item.task.RecurrenceDto
import kotlin.time.Instant

/**
 * Запрос на смену типа item. Полиморфен по целевому типу (дискриминатор "type").
 *
 * Несёт только поля целевого типа + [version] для оптимистичной блокировки.
 * Базовые поля (name, description, area, tags) уже есть у item и не передаются.
 */
@Serializable
sealed interface ConvertItemRequest {

    val version: Int

    @Serializable
    @SerialName("NOTE")
    data class ToNote(
        override val version: Int,
    ) : ConvertItemRequest

    @Serializable
    @SerialName("TASK")
    data class ToTask(
        override val version: Int,
        val dueDate: LocalDate? = null,
        val dueTime: LocalTime? = null,
        val recurrence: RecurrenceDto? = null,
    ) : ConvertItemRequest

    @Serializable
    @SerialName("EVENT")
    data class ToEvent(
        override val version: Int,
        val startDate: Instant,
        val endDate: Instant? = null,
        val isAllDay: Boolean = false,
        val location: String? = null,
    ) : ConvertItemRequest
}
