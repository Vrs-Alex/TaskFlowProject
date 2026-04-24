package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.area.AreaRepositoryImpl
import com.vrsalex.taskflow.domain.area.AreaRepository
import org.koin.dsl.module

val workspaceModule = module {

    single<AreaRepository> { AreaRepositoryImpl(get(), get(), get()) }

}