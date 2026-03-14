package vrsalex.database.core

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object AppUserTable: IdTable<Long>("app_user") {
    override val id = long("user_id").autoIncrement().entityId()
    val publicId = uuid("public_id").uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val fullName = varchar("full_name", 100).nullable()
    val passwordHash = text("password_hash")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}