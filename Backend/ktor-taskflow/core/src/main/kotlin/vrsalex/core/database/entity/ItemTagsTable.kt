package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object ItemTagsTable : LongIdTable("item_tag", "item_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val tag = reference("tag_id", TagTable, onDelete = ReferenceOption.CASCADE)
}