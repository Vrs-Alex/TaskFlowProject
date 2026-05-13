package com.vrsalex.taskflow.domain.item.note

import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemRepository
import com.vrsalex.taskflow.domain.item.base.ItemUpdate

interface NoteRepository: ItemRepository<Note, NoteCreate, NoteUpdate>