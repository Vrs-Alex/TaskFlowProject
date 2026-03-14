package vrsalex.api.dto.account

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val fullName: String? = null,
    val password: String
)
