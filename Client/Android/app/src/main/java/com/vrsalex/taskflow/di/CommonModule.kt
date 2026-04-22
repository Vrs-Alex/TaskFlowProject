package com.vrsalex.taskflow.di

import com.vrsalex.taskflow.data.local.DataStoreManagerImpl
import com.vrsalex.taskflow.domain.common.DataStoreManager
import com.vrsalex.taskflow.domain.common.GetStartDestinationUseCase
import com.vrsalex.taskflow.presentation.navigation.EntyPointViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single<DataStoreManager> { DataStoreManagerImpl(androidContext()) }

    factory { GetStartDestinationUseCase(get()) }

    viewModel { EntyPointViewModel(get()) }

}