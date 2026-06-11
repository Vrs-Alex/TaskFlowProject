package vrsalex.auth.data

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.auth.domain.model.RefreshToken
import vrsalex.auth.domain.model.RefreshTokenCreate
import vrsalex.auth.domain.repository.RefreshTokenRepository
import vrsalex.core.database.UserSessionTable
import vrsalex.core.database.utils.findOne
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
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

    override suspend fun setDeprecated(tokenId: Uuid) {
        val deadline = Clock.System.now().plus(60.seconds)
        UserSessionTable.update({
            (UserSessionTable.id eq tokenId) and (UserSessionTable.expiresAt greater deadline)
        }) {
            it[expiresAt] = deadline
        }
    }

    override suspend fun deleteExpired(): Int =
        UserSessionTable.deleteWhere { expiresAt lessEq Clock.System.now() }

}