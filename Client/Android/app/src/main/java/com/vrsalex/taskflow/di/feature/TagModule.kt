package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.workspace.tag.TagLocalDataSource
import com.vrsalex.taskflow.data.workspace.tag.TagRepositoryImpl
import com.vrsalex.taskflow.domain.workspace.tag.TagRepository
import org.koin.dsl.module
import kotlin.math.sin

val tagModule = module {

    single { TagLocalDataSource(get()) }

    single<TagRepository> { TagRepositoryImpl(get(), get(), get(), get()) }

}