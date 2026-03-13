package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp

object HabitLogsTable : LongIdTable("habit_log", "log_id") {
    val habit = reference("habit_id", HabitTable, onDelete = ReferenceOption.CASCADE)
    val logDate = date("log_date")
    val note = text("note").nullable()
    val completedAt = timestamp("completed_at").defaultExpression(CurrentTimestamp)
    val clientId = uuid("client_id")
}