package vrsalex.infrastructure.persistence.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.domain.model.RefreshToken
import vrsalex.infrastructure.persistence.table.RefreshTokenTable

fun ResultRow.toRefreshToken() = RefreshToken(
    id = this[RefreshTokenTable.id].value,
    userId = this[RefreshTokenTable.userId],
    tokenHash = this[RefreshTokenTable.tokenHash],
    deviceInfo = this[RefreshTokenTable.deviceInfo],
    ipAddress = this[RefreshTokenTable.ipAddress],
    expiresAt = this[RefreshTokenTable.expiresAt],
    createdAt = this[RefreshTokenTable.createdAt],
    lastUsedAt = this[RefreshTokenTable.lastUsedAt],
    isRevoked = this[RefreshTokenTable.isRevoked]
)