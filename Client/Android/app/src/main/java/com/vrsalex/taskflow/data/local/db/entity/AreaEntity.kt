package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "area")
data class AreaEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val color: String,
    @Embedded override val sync: SyncColumns,
): ISyncDbColumns
