package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.item.event.EventRepositoryImpl
import com.vrsalex.taskflow.domain.item.event.EventRepository
import org.koin.dsl.module

val itemModule = module {

    single<EventRepository> { EventRepositoryImpl(get(), get(), get(), get(), get()) }

}