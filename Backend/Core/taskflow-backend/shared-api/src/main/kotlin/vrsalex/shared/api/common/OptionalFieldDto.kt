package vrsalex.shared.api.common

import kotlinx.serialization.Serializable
import vrsalex.shared.api.serializer.OptionalFieldSerializer

/**
 * Обертка для опциональных полей в DTO.
 * Используется для отличия между "поле не указано" и "поле указано со значением null".
 * Применяется в PATCH запросах для обновления полей.
 * @param T - тип опционального поля
 */
@Serializable(with = OptionalFieldSerializer::class)
sealed class OptionalFieldDto<out T> {
    object Undefined : OptionalFieldDto<Nothing>()
    data class Defined<T>(val value: T) : OptionalFieldDto<T>()

    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

    fun valueOrNull(): T? = if (this is Defined) value else null
}