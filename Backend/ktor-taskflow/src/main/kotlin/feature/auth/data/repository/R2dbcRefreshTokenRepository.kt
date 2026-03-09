package vrsalex.feature.auth.data.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.findOne
import vrsalex.feature.auth.data.table.RefreshTokenTable
import vrsalex.feature.auth.data.mapper.toRefreshToken
import vrsalex.feature.auth.domain.model.RefreshToken
import vrsalex.feature.auth.domain.model.RefreshTokenCreate
import vrsalex.feature.auth.domain.repository.RefreshTokenRepository
import kotlin.time.Clock
import kotlin.uuid.Uuid

class R2dbcRefreshTokenRepository(
    private val db: R2dbcDatabase
): RefreshTokenRepository {

    override suspend fun findById(id: Uuid): RefreshToken? =
        RefreshTokenTable.findOne(db) { RefreshTokenTable.id eq id }?.toRefreshToken()

    override suspend fun findByHash(tokenHash: String): RefreshToken? =
        RefreshTokenTable.findOne(db) { RefreshTokenTable.tokenHash eq tokenHash }?.toRefreshToken()

    override suspend fun save(token: RefreshTokenCreate): Uuid =
        RefreshTokenTable.insertAndGetId {
            it[RefreshTokenTable.userId] = token.userId
            it[RefreshTokenTable.tokenHash] = token.tokenHash
            it[RefreshTokenTable.deviceInfo] = token.deviceInfo
            it[RefreshTokenTable.ipAddress] = token.ipAddress
            it[RefreshTokenTable.expiresAt] = token.expiresAt
        }.value

    override suspend fun markAsUsed(tokenHash: String): Boolean =
        RefreshTokenTable.update({ RefreshTokenTable.tokenHash eq tokenHash }) {
            it[lastUsedAt] = Clock.System.now()
        } >= 1

    override suspend fun deleteRevokedForUser(userId: Uuid): Int {
        TODO("Not yet implemented")
    }
}