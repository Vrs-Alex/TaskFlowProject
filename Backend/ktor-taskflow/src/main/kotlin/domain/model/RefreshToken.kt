package vrsalex.domain.model

import kotlin.time.Instant

data class RefreshToken(
    val id: Long,
    val userId: Long,
    val tokenHash: String,
    val deviceInfo: String?,
    val ipAddress: String?,
    val expiresAt: Instant,
    val createdAt: Instant,
    val lastUsedAt: Instant?,
    val isRevoked: Boolean
)

data class RefreshTokenCreate(
    val userId: Long,
    val tokenHash: String,
    val deviceInfo: String?,
    val ipAddress: String?,
    val expiresAt: Instant
)