package vrsalex.auth.domain.repository

import vrsalex.auth.domain.model.auth.RefreshToken
import vrsalex.auth.domain.model.auth.RefreshTokenCreate
import kotlin.uuid.Uuid

interface RefreshTokenRepository {

    suspend fun findById(id: Uuid): RefreshToken?
    suspend fun findByHash(tokenHash: String): RefreshToken?

    suspend fun save(token: RefreshTokenCreate): Uuid
    suspend fun deleteByTokenId(tokenId: Uuid): Boolean

}