package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.ItemTable

object AttachmentTable : LongIdTable("attachment", "attachment_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val fileName = varchar("file_name", length = 255)
    val filePath = text("file_path")
    val fileType = varchar("file_type", length = 100).nullable()
    val fileSize = long("file_size").nullable()
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}