package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.workspace.area.AreaRepositoryImpl
import com.vrsalex.taskflow.data.workspace.tag.TagRepositoryImpl
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import org.koin.dsl.module

val workspaceModule = module {

    single<AreaRepository> { AreaRepositoryImpl(get(), get(), get()) }

    single<TagRepository> { TagRepositoryImpl(get(), get(), get()) }

}