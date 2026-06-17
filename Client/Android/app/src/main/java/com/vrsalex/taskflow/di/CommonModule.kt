package com.vrsalex.taskflow.di

import com.vrsalex.taskflow.domain.common.start.GetStartDestinationUseCase
import com.vrsalex.taskflow.presentation.navigation.AppEntryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {

    single { GetStartDestinationUseCase(get()) }

    viewModelOf(::AppEntryViewModel)

}