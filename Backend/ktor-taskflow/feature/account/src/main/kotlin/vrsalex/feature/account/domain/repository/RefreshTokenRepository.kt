package vrsalex.feature.account.domain.repository

import vrsalex.feature.account.domain.model.RefreshToken
import vrsalex.feature.account.domain.model.RefreshTokenCreate
import kotlin.uuid.Uuid

interface RefreshTokenRepository {

    suspend fun findById(id: Uuid): RefreshToken?
    suspend fun findByHash(tokenHash: String): RefreshToken?
    suspend fun save(token: RefreshTokenCreate): Uuid
    suspend fun markAsUsed(tokenHash: String): Boolean
    suspend fun deleteByTokenId(tokenId: Uuid): Boolean
    suspend fun revoke(tokenId: Uuid): Boolean
    suspend fun deleteRevokedForUser(userId: Uuid): Int

}