package vrsalex.core.database

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object ReminderTable : LongIdTable("reminder", "id"), SyncTable {
    override val userId: Column<EntityID<Long>> = reference("user_id", UserTable)
    override val clientId = uuid("client_id")
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)
    override val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    val itemId = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val remindAt = timestamp("remind_at").defaultExpression(CurrentTimestamp)
    val isDone = bool("is_done").default(false)
}