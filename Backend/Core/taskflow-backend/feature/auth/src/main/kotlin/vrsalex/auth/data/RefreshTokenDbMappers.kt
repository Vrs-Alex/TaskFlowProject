package vrsalex.auth.data

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.auth.domain.model.auth.RefreshToken
import vrsalex.core.database.UserSessionTable


fun ResultRow.toRefreshToken() = RefreshToken(
    id = this[UserSessionTable.id].value,
    userId = this[UserSessionTable.userId].value,
    tokenHash = this[UserSessionTable.tokenHash],
    agent =  this[UserSessionTable.agent],
    ipAddress = this[UserSessionTable.ipAddress],
    expiresAt = this[UserSessionTable.expiresAt],
    createdAt = this[UserSessionTable.createdAt]
)