package vrsalex.shared.api.exception

import kotlinx.serialization.Serializable


@Serializable
data class ServerErrorResponse(
    val status: ServerStatusCode,
    val code: ErrorCode? = null,
    val message: String? = null
)
