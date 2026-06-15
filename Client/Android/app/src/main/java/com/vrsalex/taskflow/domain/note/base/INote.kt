package com.vrsalex.taskflow.domain.note.base

import com.vrsalex.taskflow.domain.sync.ISyncModel
import com.vrsalex.taskflow.domain.sync.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.SyncModel
import com.vrsalex.taskflow.domain.sync.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.SyncModelUpdate

interface INote: ISyncModel {
    val note: Note
    override val syncModel: SyncModel
        get() = note.syncModel
}

interface INoteCreate: ISyncModelCreate {
    val note: NoteCreate
    override val syncModelCreate: SyncModelCreate
        get() = note.syncModelCreate
}

interface INoteUpdate: ISyncModelUpdate {
    val note: NoteUpdate
    override val syncModelUpdate: SyncModelUpdate
        get() = note.syncModelUpdate
}