package vrsalex.feature.account.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class RefreshToken(
    val id: Uuid,
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
    val tokenId: Uuid,
    val userId: Long,
    val tokenHash: String,
    val deviceInfo: String?,
    val ipAddress: String?,
    val expiresAt: Instant
)

data class JwtTokens(
    val accessToken: String,
    val refreshToken: String
)

data class TokenGenerationResult(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenId: Uuid
)

