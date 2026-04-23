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

suspend fun <T> ModelDto<T>.isActive(block: suspend ModelDto.Active<T>.() -> Unit) {
    if (this is ModelDto.Active) block()
}

suspend fun <T> ModelDto<T>.isDeleted(block: suspend ModelDto.Deleted.() -> Unit) {
    if (this is ModelDto.Deleted) block()
}