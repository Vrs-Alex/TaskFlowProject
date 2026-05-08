package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.item.event.EventRepositoryImpl
import com.vrsalex.taskflow.data.item.task.TaskLogRepositoryImpl
import com.vrsalex.taskflow.data.item.task.TaskRepositoryImpl
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLogLocalDataSource
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.task.TaskLogRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import org.koin.dsl.module

val itemModule = module {

    single<EventRepository> { EventRepositoryImpl(get(), get(), get(), get(), get()) }

    single<TaskLogRepository> { TaskLogRepositoryImpl(get(), get(), get(), get(), get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get(), get(), get(), get(), get()) }

}
