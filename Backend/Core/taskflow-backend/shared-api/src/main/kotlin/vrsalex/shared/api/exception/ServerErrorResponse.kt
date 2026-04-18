package vrsalex.shared.api.exception

import kotlinx.serialization.Serializable


@Serializable
data class ServerErrorResponse(
    val status: ServerStatusCode,
    val message: String? = null
)
