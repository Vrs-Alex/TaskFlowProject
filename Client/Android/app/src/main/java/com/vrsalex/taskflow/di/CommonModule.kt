package com.vrsalex.taskflow.di

import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.data.local.datastore.DataStoreManagerImpl
import com.vrsalex.taskflow.data.remote.AuthObserverImpl
import com.vrsalex.taskflow.data.remote.DeviceIdProviderImpl
import com.vrsalex.taskflow.data.remote.TokenProviderImpl
import com.vrsalex.taskflow.domain.common.notify.AppMessenger
import com.vrsalex.taskflow.domain.common.start.GetStartDestinationUseCase
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import com.vrsalex.taskflow.presentation.common.AppMessengerImpl
import com.vrsalex.taskflow.presentation.navigation.EntyPointViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {

    single<DataStoreManager> { DataStoreManagerImpl(androidContext()) }

    factory { GetStartDestinationUseCase(get()) }

    viewModel { EntyPointViewModel(get(), get(), get(), get(), get()) }

    single<TokenProvider> { TokenProviderImpl(get()) }

    single<AuthObserver> { AuthObserverImpl() }

    single<DeviceIdProvider> { DeviceIdProviderImpl(get()) }

    single<AppMessenger> { AppMessengerImpl() }

}