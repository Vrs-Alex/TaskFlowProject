package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "tag")
data class TagEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val color: String,
    @Embedded override val sync: SyncColumns,
): ISyncDbColumns