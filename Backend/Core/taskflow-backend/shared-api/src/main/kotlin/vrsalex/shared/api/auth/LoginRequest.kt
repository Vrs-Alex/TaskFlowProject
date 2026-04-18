package vrsalex.shared.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identity: String,
    val password: String
)
