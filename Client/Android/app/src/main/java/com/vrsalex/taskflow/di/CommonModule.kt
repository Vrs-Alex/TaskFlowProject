package com.vrsalex.taskflow.di

import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.data.local.datastore.DataStoreManagerImpl
import com.vrsalex.taskflow.data.remote.AuthObserverImpl
import com.vrsalex.taskflow.data.remote.DeviceIdProviderImpl
import com.vrsalex.taskflow.data.remote.TokenProviderImpl
import com.vrsalex.taskflow.domain.common.ui_messages.AppMessenger
import com.vrsalex.taskflow.domain.common.start.GetStartDestinationUseCase
import com.vrsalex.taskflow.domain.common.storage.DataStoreManager
import com.vrsalex.taskflow.presentation.common.AppMessengerImpl
import com.vrsalex.taskflow.presentation.navigation.EntryPointViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {

    single<DataStoreManager> { DataStoreManagerImpl(androidContext()) }

    single<TokenProvider> { TokenProviderImpl(get()) }

    single<AuthObserver> { AuthObserverImpl(get(), get()) }

    single<DeviceIdProvider> { DeviceIdProviderImpl(androidContext(), get()) }

    factory { GetStartDestinationUseCase(get()) }

    viewModelOf(::EntryPointViewModel)

    single<AppMessenger> { AppMessengerImpl() }

}