package com.vrsalex.taskflow.domain.note.base

import com.vrsalex.taskflow.domain.common.OptionalField
import com.vrsalex.taskflow.domain.common.validation.note.NoteDescription
import com.vrsalex.taskflow.domain.common.validation.note.NoteName
import com.vrsalex.taskflow.domain.sync.ISyncModel
import com.vrsalex.taskflow.domain.sync.ISyncModelCreate
import com.vrsalex.taskflow.domain.sync.ISyncModelUpdate
import com.vrsalex.taskflow.domain.sync.SyncModel
import com.vrsalex.taskflow.domain.sync.SyncModelCreate
import com.vrsalex.taskflow.domain.sync.SyncModelUpdate
import com.vrsalex.taskflow.domain.workspace.area.Area
import com.vrsalex.taskflow.domain.workspace.tag.Tag

data class Note(
    val name: NoteName,
    val description: NoteDescription?,
    val type: NoteType,
    val status: NoteStatus,
    val priority: Short,
    val area: Area?,
    val tags: List<Tag>,
    override val syncModel: SyncModel
): ISyncModel

data class NoteCreate(
    val name: NoteName,
    val description: NoteDescription?,
    val type: NoteType,
    val status: NoteStatus,
    val priority: Short,
    val area: Area?,
    val tags: List<Tag>,
    override val syncModelCreate: SyncModelCreate
): ISyncModelCreate

data class NoteUpdate(
    val name: OptionalField<NoteName> = OptionalField.Undefined,
    val description: OptionalField<NoteDescription?> = OptionalField.Undefined,
    val type: OptionalField<NoteType> = OptionalField.Undefined,
    val status: OptionalField<NoteStatus> = OptionalField.Undefined,
    val priority: OptionalField<Short> = OptionalField.Undefined,
    val area: OptionalField<Area?> = OptionalField.Undefined,
    val tags: OptionalField<List<Tag>> = OptionalField.Undefined,
    override val syncModelUpdate: SyncModelUpdate
): ISyncModelUpdate