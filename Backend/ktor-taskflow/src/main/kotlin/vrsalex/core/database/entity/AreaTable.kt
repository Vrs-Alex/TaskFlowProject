package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object AreaTable : LongIdTable("area", "area_id") {
    val owner = reference("owner_id", AppUserTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", length = 100)
    val color = varchar("color", length = 7).default("'#FFFFFF'")
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}