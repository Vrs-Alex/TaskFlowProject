package com.vrsalex.taskflow.data.local.db.datasource.item

import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlin.uuid.Uuid

class NoteLocalDataSource(private val db: AppDatabase) {

    fun getNotes() = db.itemDao().getItemsByType(ItemType.NOTE)

    fun getNote(id: Uuid) = db.itemDao().getItem(id)
}
