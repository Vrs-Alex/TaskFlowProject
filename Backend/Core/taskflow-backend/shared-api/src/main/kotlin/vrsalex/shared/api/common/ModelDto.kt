package vrsalex.shared.api.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid


@Serializable
sealed class ModelDto<out T> {

    @Serializable
    @SerialName("active")
    data class Active<T>(val data: T) : ModelDto<T>()


    @Serializable
    @SerialName("deleted")
    data class Deleted(
        val id: Long,
        val clientId: Uuid,
        val version: Int
    ) : ModelDto<Nothing>()
}