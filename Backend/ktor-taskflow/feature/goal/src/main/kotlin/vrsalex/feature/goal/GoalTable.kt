package vrsalex.feature.goal

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.ItemTable
import java.math.BigDecimal

object GoalTable : LongIdTable("goal", "item_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val targetValue = decimal("target_value", precision = 12, scale = 2)
    val currentValue = decimal("current_value", precision = 12, scale = 2).default(BigDecimal.ZERO)
    val unit = reference("unit_id", GoalUnitTable)
    val startDate = date("start_date").nullable()
    val deadline = date("deadline").nullable()
    val completedAt = timestamp("completed_at").defaultExpression(CurrentTimestamp).nullable()
}