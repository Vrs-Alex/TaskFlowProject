package vrsalex.feature.auth.domain.repository

import vrsalex.feature.auth.domain.model.RefreshToken
import vrsalex.feature.auth.domain.model.RefreshTokenCreate

interface RefreshTokenRepository {

    suspend fun findById(id: Long): RefreshToken?
    suspend fun findByHash(tokenHash: String): RefreshToken?
    suspend fun save(token: RefreshTokenCreate): Long
    suspend fun markAsUsed(tokenHash: String): Boolean
//    suspend fun revoke(tokenId: Long): Boolean
    suspend fun deleteRevokedForUser(userId: Long): Int

}