package com.vrsalex.taskflow.domain.note.base

import com.vrsalex.taskflow.domain.sync.SyncRepository

interface NoteRepository : SyncRepository<Note, NoteCreate, NoteUpdate>