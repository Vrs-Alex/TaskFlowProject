package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.database.core.manual.ProjectStatusTable

object ProjectTable : LongIdTable("project", "project_id"), SyncTable{
    override val ownerId = reference("owner_id", AppUserTable, onDelete = ReferenceOption.CASCADE)
    override val clientId = uuid("client_id")
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)
    val area = reference("area_id", AreaTable, onDelete = ReferenceOption.SET_NULL)
    val status = reference("status_id", ProjectStatusTable)
    val name = varchar("name", length = 255)
    val color = varchar("color", length = 7)
    val description = text("description").nullable()
    val dueDate = date("due_date").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}
