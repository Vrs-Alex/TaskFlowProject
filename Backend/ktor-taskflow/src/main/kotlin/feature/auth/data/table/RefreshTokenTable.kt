package vrsalex.feature.auth.data.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.uuid.Uuid

object RefreshTokenTable: IdTable<Uuid>("refresh_token") {
    override val id = uuid("token_id").autoIncrement().entityId()
    val userId = long("user_id").references(AppUserTable.id, onDelete = ReferenceOption.CASCADE)
    val tokenHash = text("token_hash").uniqueIndex()
    val deviceInfo = varchar("device_info", 255).nullable()
    val ipAddress = varchar("ip_address", 45).nullable()
    val expiresAt = timestamp("expires_at")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val lastUsedAt = timestamp("last_used_at").nullable()
    val isRevoked = bool("is_revoked").default(false)
}