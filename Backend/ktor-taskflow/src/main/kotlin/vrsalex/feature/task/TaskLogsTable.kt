package vrsalex.feature.task

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp

object TaskLogsTable : LongIdTable("task_log", "log_id") {
    val task = reference("task_id", TaskTable, onDelete = ReferenceOption.CASCADE)
    val logDate = date("log_date")
    val note = text("note").nullable()
    val completedAt = timestamp("completed_at").defaultExpression(CurrentTimestamp)
    val clientId = uuid("client_id")
}