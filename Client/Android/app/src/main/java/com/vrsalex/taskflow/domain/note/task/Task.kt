package com.vrsalex.taskflow.domain.note.task

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.note.base.INote
import com.vrsalex.taskflow.domain.note.base.INoteCreate
import com.vrsalex.taskflow.domain.note.base.INoteUpdate
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class Task(
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrenceType: RecurrenceType?,
    val recurrenceDays: Short?,
    val recurrenceEndDate: LocalDate?,
    val recurrenceCount: Int?,
    val isCompleted: Boolean,
    override val note: Note
): INote

data class TaskCreate(
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrenceType: RecurrenceType?,
    val recurrenceDays: Short?,
    val recurrenceEndDate: LocalDate?,
    val recurrenceCount: Int?,
    override val note: NoteCreate
): INoteCreate

data class TaskUpdate(
    val dueDate: OptionalField<LocalDate?> = OptionalField.Undefined,
    val dueTime: OptionalField<LocalTime?> = OptionalField.Undefined,
    val recurrenceType: OptionalField<RecurrenceType?> = OptionalField.Undefined,
    val recurrenceDays: OptionalField<Short?> = OptionalField.Undefined,
    val recurrenceEndDate: OptionalField<LocalDate?> = OptionalField.Undefined,
    val recurrenceCount: OptionalField<Int?> = OptionalField.Undefined,
    override val note: NoteUpdate
): INoteUpdate