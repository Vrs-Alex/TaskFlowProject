package com.vrsalex.taskflow.domain.note.event

import com.vrsalex.taskflow.domain.common.OptionalField
import com.vrsalex.taskflow.domain.note.base.INote
import com.vrsalex.taskflow.domain.note.base.INoteCreate
import com.vrsalex.taskflow.domain.note.base.INoteUpdate
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import kotlin.time.Instant

data class Event(
    val startDate: Instant,
    val endDate: Instant?,
    val isAllDay: Boolean,
    val location: String?,
    override val note: Note
): INote

data class EventCreate(
    val startDate: Instant,
    val endDate: Instant?,
    val isAllDay: Boolean,
    val location: String?,
    override val note: NoteCreate
): INoteCreate

data class EventUpdate(
    val startDate: OptionalField<Instant> = OptionalField.Undefined,
    val endDate: OptionalField<Instant?> = OptionalField.Undefined,
    val isAllDay: OptionalField<Boolean> = OptionalField.Undefined,
    val location: OptionalField<String?> = OptionalField.Undefined,
    override val note: NoteUpdate
): INoteUpdate