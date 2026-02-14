package vrsalex.domain.model

import kotlin.time.Instant


data class User(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String?,
    val createdAt: Instant
)

data class UserCreate(
    val username: String,
    val email: String,
    val fullName: String?,
    val passwordHash: String
)