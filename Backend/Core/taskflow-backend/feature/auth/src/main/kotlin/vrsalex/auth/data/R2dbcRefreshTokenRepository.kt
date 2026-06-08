package vrsalex.auth.data

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import vrsalex.auth.domain.model.auth.RefreshToken
import vrsalex.auth.domain.model.auth.RefreshTokenCreate
import vrsalex.auth.domain.repository.RefreshTokenRepository
import vrsalex.core.database.UserSessionTable
import vrsalex.core.database.utils.findOne
import kotlin.uuid.Uuid

class R2dbcRefreshTokenRepository: RefreshTokenRepository {

    override suspend fun findById(id: Uuid): RefreshToken? =
        UserSessionTable.findOne { UserSessionTable.id eq id }?.toRefreshToken()

    override suspend fun findByHash(tokenHash: String): RefreshToken? =
        UserSessionTable.findOne { UserSessionTable.tokenHash eq tokenHash }?.toRefreshToken()

    override suspend fun save(token: RefreshTokenCreate): Uuid =
        UserSessionTable.insertAndGetId {
            it[UserSessionTable.id] = token.tokenId
            it[UserSessionTable.userId] = token.userId
            it[UserSessionTable.tokenHash] = token.tokenHash
            it[UserSessionTable.agent] = token.agent
            it[UserSessionTable.ipAddress] = token.ipAddress
            it[UserSessionTable.expiresAt] = token.expiresAt
        }.value


    override suspend fun deleteByTokenId(tokenId: Uuid): Boolean =
        UserSessionTable.deleteWhere { UserSessionTable.id eq tokenId } >= 1

}