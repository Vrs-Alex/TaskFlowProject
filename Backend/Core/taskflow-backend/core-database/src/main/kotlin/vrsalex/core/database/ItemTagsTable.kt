package vrsalex.core.database

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable


object ItemTagsTable : Table("item_tag") {
    val itemId = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val tagId = reference("tag_id", TagTable, onDelete = ReferenceOption.CASCADE)
}