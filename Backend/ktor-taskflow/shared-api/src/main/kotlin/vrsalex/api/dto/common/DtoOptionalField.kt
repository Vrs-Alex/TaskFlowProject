package vrsalex.api.dto.common

import kotlinx.serialization.Serializable
import vrsalex.api.serializer.OptionalFieldSerializer

/**
 * Обертка для опциональных полей в DTO.
 * Используется для отличия между "поле не указано" и "поле указано со значением null".
 * Применяется в PATCH запросах для обновления полей.
 * @param T - тип опционального поля
 */
@Serializable(with = OptionalFieldSerializer::class)
sealed class DtoOptionalField<out T> {
    object Undefined : DtoOptionalField<Nothing>()
    data class Defined<T>(val value: T) : DtoOptionalField<T>()


    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

    fun valueOrNull(): T? = if (this is Defined) value else null
}