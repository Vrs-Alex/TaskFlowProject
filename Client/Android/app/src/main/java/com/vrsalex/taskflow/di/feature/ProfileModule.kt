package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.profile.ProfileRepositoryImpl
import com.vrsalex.taskflow.domain.profile.ProfileRepository
import org.koin.dsl.module

val profileModule = module {

    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }

}