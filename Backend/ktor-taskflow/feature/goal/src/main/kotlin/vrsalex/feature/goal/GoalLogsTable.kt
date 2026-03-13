package vrsalex.feature.goal

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object GoalLogsTable : LongIdTable("goal_log", "log_id") {
    val goal = reference("goal_id", GoalTable, onDelete = ReferenceOption.CASCADE)
    val valueChange = decimal("value_change", precision = 12, scale = 2)
    val note = text("note").nullable()
    val recordedAt = timestamp("recorded_at").defaultExpression(CurrentTimestamp)
    val clientId = uuid("client_id")
}