package com.vrsalex.network.di

import com.vrsalex.network.internal.impl.AuthApiImpl
import com.vrsalex.network.public.api.auth.AuthApi
import org.koin.dsl.module

internal val apiModule = module {

    single<AuthApi> { AuthApiImpl(get()) }

}