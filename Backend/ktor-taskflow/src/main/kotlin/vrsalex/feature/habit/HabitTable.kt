package vrsalex.feature.habit

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.date
import vrsalex.core.database.entity.ItemTable

object HabitTable : LongIdTable("habit", "item_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val startDate = date("start_date").nullable()
    val endDate = date("end_date").nullable()
    val isActive = bool("is_active").default(true)
}