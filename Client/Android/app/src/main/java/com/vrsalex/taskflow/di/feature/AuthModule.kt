package com.vrsalex.taskflow.di.feature

import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.network.public.provider.DeviceIdProvider
import com.vrsalex.network.public.provider.TokenProvider
import com.vrsalex.taskflow.data.auth.AuthObserverImpl
import com.vrsalex.taskflow.data.auth.AuthRepositoryImpl
import com.vrsalex.taskflow.data.auth.DeviceIdProviderImpl
import com.vrsalex.taskflow.data.auth.TokenProviderImpl
import com.vrsalex.taskflow.domain.auth.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authModule = module {

    single<TokenProvider> { TokenProviderImpl(get()) }
    single<AuthObserver> { AuthObserverImpl() }
    single<DeviceIdProvider> { DeviceIdProviderImpl(androidContext()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
}
