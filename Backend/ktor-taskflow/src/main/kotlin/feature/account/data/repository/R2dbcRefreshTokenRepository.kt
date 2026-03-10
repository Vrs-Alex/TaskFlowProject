package feature.account.data.repository

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.findOne
import feature.account.data.table.RefreshTokenTable
import feature.account.data.mapper.toRefreshToken
import feature.account.domain.model.RefreshToken
import feature.account.domain.model.RefreshTokenCreate
import feature.account.domain.repository.RefreshTokenRepository
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
            it[RefreshTokenTable.id] = token.tokenId
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

    override suspend fun deleteByTokenId(tokenId: Uuid): Boolean =
        RefreshTokenTable.deleteWhere { RefreshTokenTable.id eq tokenId } >= 1

    override suspend fun revoke(tokenId: Uuid): Boolean =
        RefreshTokenTable.update({ RefreshTokenTable.id eq tokenId }) {
            it[isRevoked] = true
        } >= 1

    override suspend fun deleteRevokedForUser(userId: Uuid): Int {
        TODO("Not yet implemented")
    }
}