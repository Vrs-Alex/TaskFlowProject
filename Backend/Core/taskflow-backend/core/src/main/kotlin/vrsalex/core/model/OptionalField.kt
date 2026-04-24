package vrsalex.core.model

import vrsalex.shared.api.common.OptionalFieldDto

sealed class OptionalField<out T> {

    data object Undefined: OptionalField<Nothing>()
    data class Defined<T>(val value: T): OptionalField<T>()

    inline fun <R> map(mapper: (T) -> R): OptionalField<R> = when (this) {
        is Undefined -> Undefined
        is Defined -> Defined(mapper(value))
    }

    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

    suspend inline fun onSuspendDefined(block: suspend (T) -> Unit) {
        if (this is Defined) block(value)
    }

}

fun <T> OptionalFieldDto<T>.toOptional(): OptionalField<T> = when (this) {
    is OptionalFieldDto.Defined -> OptionalField.Defined(this.value)
    OptionalFieldDto.Undefined -> OptionalField.Undefined
}


fun isAnyDefined(vararg fields: OptionalField<*>): Boolean {
    return fields.any { it is OptionalField.Defined }
}