package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.auth.AuthRepositoryImpl
import com.vrsalex.taskflow.domain.auth.AuthRepository
import org.koin.dsl.module

val authModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get(), get()) }

}