package vrsalex.api.dto.account

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identity: String,
    val password: String
)
