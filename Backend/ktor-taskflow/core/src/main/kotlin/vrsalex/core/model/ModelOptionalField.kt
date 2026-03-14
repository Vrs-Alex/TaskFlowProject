package vrsalex.core.model

import vrsalex.api.dto.common.DtoOptionalField

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

fun <T> DtoOptionalField<T>.toModelOptional(): ModelOptionalField<T> = when (this) {
    is DtoOptionalField.Defined -> ModelOptionalField.Defined(this.value)
    DtoOptionalField.Undefined -> ModelOptionalField.Undefined
}