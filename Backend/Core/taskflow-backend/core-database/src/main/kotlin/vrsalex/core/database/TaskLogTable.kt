package vrsalex.core.database

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp

object TaskLogTable : LongIdTable("task_log", "id"), SyncTable {
    override val userId = reference("user_id", UserTable)
    override val clientId = uuid("client_id").uniqueIndex()
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)
    override val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    val task = optReference("task_id", TaskTable)
    val clientTaskId = uuid("client_task_id")
    val date = date("date")
    val completedAt = timestamp("completed_at").nullable()
}