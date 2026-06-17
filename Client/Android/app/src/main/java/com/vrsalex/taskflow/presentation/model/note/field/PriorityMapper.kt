package com.vrsalex.taskflow.presentation.model.note.field

import com.vrsalex.taskflow.domain.note.base.NotePriority

fun NotePriority.toUi(): PriorityUi? = color?.let { PriorityUi(title, it) }
