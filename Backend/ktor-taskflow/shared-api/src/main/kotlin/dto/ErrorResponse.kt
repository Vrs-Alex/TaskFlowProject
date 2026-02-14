package dto

import error.ServerErrorCode
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val code: ServerErrorCode,
    val message: String? = null,
    val details: String? = null
)