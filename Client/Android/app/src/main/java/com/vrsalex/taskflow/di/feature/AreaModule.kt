package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.workspace.area.AreaLocalDataSource
import com.vrsalex.taskflow.data.workspace.area.AreaRepositoryImpl
import com.vrsalex.taskflow.domain.workspace.area.AreaRepository
import org.koin.dsl.module


val areaModule = module {

    single { AreaLocalDataSource(get()) }

    single<AreaRepository> { AreaRepositoryImpl(get()) }

}