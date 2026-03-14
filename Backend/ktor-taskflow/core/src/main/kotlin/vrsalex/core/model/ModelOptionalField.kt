package vrsalex.core.model

import vrsalex.api.dto.common.DtoOptionalField

/**
 * Базовый класс для опциональных полей в модели.
 * Используется для представления полей, которые могут быть не определены или иметь значение null.
 * Преобразуется из [[DtoOptionalField]] при маппинге данных из DTO в бизнес модель.
 * @param T - тип опционального поля
 */
sealed class ModelOptionalField<out T> {
    object Undefined : ModelOptionalField<Nothing>()
    data class Defined<T>(val value: T) : ModelOptionalField<T>()

    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

    inline fun <R> map(transform: (T) -> R): ModelOptionalField<R> {
        return when (this) {
            is Undefined -> Undefined
            is Defined -> Defined(transform(value))
        }
    }
}

/**
 * Преобразует опциональное поле из DTO в бизнес модель.
 * @param T - тип опционального поля
 * @return экземпляр ModelOptionalField с соответствующим значением
 */
fun <T> DtoOptionalField<T>.toModelOptional(): ModelOptionalField<T> = when (this) {
    is DtoOptionalField.Defined -> ModelOptionalField.Defined(this.value)
    DtoOptionalField.Undefined -> ModelOptionalField.Undefined
}