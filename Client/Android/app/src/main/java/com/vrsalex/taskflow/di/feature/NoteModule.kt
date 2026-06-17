package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.note.NoteLocalDataSource
import com.vrsalex.taskflow.data.note.NoteRepositoryImpl
import com.vrsalex.taskflow.data.note.event.EventLocalDataSource
import com.vrsalex.taskflow.data.note.event.EventRepositoryImpl
import com.vrsalex.taskflow.data.note.task.TaskLocalDataSource
import com.vrsalex.taskflow.data.note.task.TaskLogLocalDataSource
import com.vrsalex.taskflow.data.note.task.TaskRepositoryImpl
import com.vrsalex.taskflow.data.workspace.tag.TagLocalDataSource
import com.vrsalex.taskflow.data.workspace.tag.TagRepositoryImpl
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.workspace.tag.TagRepository
import org.koin.dsl.module


val noteModule = module {

    single { NoteLocalDataSource(get()) }

    single<NoteRepository> { NoteRepositoryImpl(get(), get(), get()) }


    single { TaskLocalDataSource(get()) }
    single { TaskLogLocalDataSource(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get()) }



    single { EventLocalDataSource(get()) }

    single<EventRepository> { EventRepositoryImpl(get(), get(), get()) }

}