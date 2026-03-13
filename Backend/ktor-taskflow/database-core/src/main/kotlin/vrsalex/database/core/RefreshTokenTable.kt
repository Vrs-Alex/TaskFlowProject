package vrsalex.database.core

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.AppUserTable
import kotlin.uuid.Uuid

object RefreshTokenTable : IdTable<Uuid>("refresh_token") {
    override val id = uuid("token_id").entityId()
    val user = reference("user_id", AppUserTable)
    val tokenHash = text("token_hash")
    val deviceInfo = varchar("device_info", length = 255).nullable()
    val ipAddress = varchar("ip_address", length = 45).nullable()
    val expiresAt = timestamp("expires_at").defaultExpression(CurrentTimestamp)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val lastUsedAt = timestamp("last_used_at").defaultExpression(CurrentTimestamp).nullable()
    val isRevoked = bool("is_revoked").default(false)
}