package com.vrsalex.taskflow.domain.common.model

sealed class OptionalField<out T>  {

    data object Undefined: OptionalField<Nothing>()
    data class Defined<T>(val value: T): OptionalField<T>()

    inline fun <R> map(mapper: (T) -> R): OptionalField<R> = when (this) {
        is Undefined -> Undefined
        is Defined -> Defined(mapper(value))
    }

    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

}

