package vrsalex.api.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import vrsalex.api.dto.common.DtoOptionalField

class OptionalFieldSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<DtoOptionalField<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun serialize(encoder: Encoder, value: DtoOptionalField<T>) {
        when (value) {
            is DtoOptionalField.Undefined -> {
                throw SerializationException("Undefined field should not be serialized. Please, set `encodeDefaults` to false in Json configuration")
            }
            is DtoOptionalField.Defined -> {
                encoder.encodeSerializableValue(dataSerializer, value.value)
            }
        }
    }

    override fun deserialize(decoder: Decoder): DtoOptionalField<T> {
        return DtoOptionalField.Defined(decoder.decodeSerializableValue(dataSerializer))
    }
}
