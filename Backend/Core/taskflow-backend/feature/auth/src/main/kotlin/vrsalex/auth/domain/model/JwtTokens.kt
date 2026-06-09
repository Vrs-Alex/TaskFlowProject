package vrsalex.auth.domain.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class RefreshToken(
    val id: Uuid,
    val userId: Long,
    val tokenHash: String,
    val agent: String,
    val ipAddress: String,
    val expiresAt: Instant,
    val createdAt: Instant
)

data class RefreshTokenCreate(
    val tokenId: Uuid,
    val userId: Long,
    val tokenHash: String,
    val agent: String,
    val ipAddress: String,
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

