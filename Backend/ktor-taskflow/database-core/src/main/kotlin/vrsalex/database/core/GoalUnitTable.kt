
package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.AppUserTable

object GoalUnitTable : IntIdTable("goal_unit", "unit_id") {
    val owner = reference("owner_id", AppUserTable, ReferenceOption.CASCADE)
    val code = varchar("code", length = 20)
    val symbol = varchar("symbol", length = 10)
    val name = varchar("name", length = 50)
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}