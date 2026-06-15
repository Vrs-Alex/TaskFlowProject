package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.NoteEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteTagCrossRef
import com.vrsalex.taskflow.data.local.db.relation.NoteRelation
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface NoteDao {

    @Upsert suspend fun upsert(note: NoteEntity)

    @Upsert suspend fun upsertTags(refs: List<NoteTagCrossRef>)

    @Query("DELETE FROM note_tag WHERE noteId = :id")
    suspend fun clearTags(id: Uuid)


    @Transaction
    suspend fun upsertWithTags(note: NoteEntity, tagIds: List<Uuid>) {
        upsert(note)
        clearTags(note.id)
        if (tagIds.isNotEmpty()) upsertTags(tagIds.map { NoteTagCrossRef(note.id, it) })
    }

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getRaw(id: Uuid): NoteEntity?

    @Transaction
    @Query("SELECT * FROM note WHERE id = :id AND isDeleted = 0")
    fun observe(id: Uuid): Flow<NoteRelation?>

    @Transaction
    @Query("SELECT * FROM note WHERE type = 'NOTE' AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<NoteRelation>>

    @Query("UPDATE note SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: Uuid)
}
