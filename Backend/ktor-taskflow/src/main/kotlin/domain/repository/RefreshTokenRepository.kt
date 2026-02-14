package vrsalex.domain.repository

import vrsalex.domain.model.RefreshToken
import vrsalex.domain.model.RefreshTokenCreate
import kotlin.time.Instant

interface RefreshTokenRepository {
    suspend fun create(token: RefreshTokenCreate): Long
    suspend fun findByHash(tokenHash: String): RefreshToken?
    suspend fun markAsUsed(tokenId: Long, usedAt: Instant): Boolean
    suspend fun revoke(tokenId: Long): Boolean
    suspend fun deleteRevokedForUser(userId: Long): Int
}