package vrsalex.feature.auth.domain.repository

import vrsalex.feature.auth.domain.model.RefreshToken
import vrsalex.feature.auth.domain.model.RefreshTokenCreate
import kotlin.uuid.Uuid

interface RefreshTokenRepository {

    suspend fun findById(id: Uuid): RefreshToken?
    suspend fun findByHash(tokenHash: String): RefreshToken?
    suspend fun save(token: RefreshTokenCreate): Uuid
    suspend fun markAsUsed(tokenHash: String): Boolean
//    suspend fun revoke(tokenId: Long): Boolean
    suspend fun deleteRevokedForUser(userId: Uuid): Int

}