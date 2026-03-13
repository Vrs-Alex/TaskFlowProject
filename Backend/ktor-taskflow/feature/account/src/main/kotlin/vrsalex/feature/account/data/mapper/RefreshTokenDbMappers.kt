package vrsalex.feature.account.data.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.database.core.RefreshTokenTable
import vrsalex.feature.account.domain.model.RefreshToken

fun ResultRow.toRefreshToken() = RefreshToken(
    id = this[RefreshTokenTable.id].value,
    userId = this[RefreshTokenTable.user].value,
    tokenHash = this[RefreshTokenTable.tokenHash],
    deviceInfo = this[RefreshTokenTable.deviceInfo],
    ipAddress = this[RefreshTokenTable.ipAddress],
    expiresAt = this[RefreshTokenTable.expiresAt],
    createdAt = this[RefreshTokenTable.createdAt],
    lastUsedAt = this[RefreshTokenTable.lastUsedAt],
    isRevoked = this[RefreshTokenTable.isRevoked]
)