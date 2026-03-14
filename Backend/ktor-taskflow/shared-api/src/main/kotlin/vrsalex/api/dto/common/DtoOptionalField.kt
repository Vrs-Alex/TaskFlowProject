package vrsalex.api.dto.common

import kotlinx.serialization.Serializable
import vrsalex.api.serializer.OptionalFieldSerializer

@Serializable(with = OptionalFieldSerializer::class)
sealed class DtoOptionalField<out T> {
    object Undefined : DtoOptionalField<Nothing>()
    data class Defined<T>(val value: T) : DtoOptionalField<T>()


    inline fun onDefined(block: (T) -> Unit) {
        if (this is Defined) block(value)
    }

    fun valueOrNull(): T? = if (this is Defined) value else null
}