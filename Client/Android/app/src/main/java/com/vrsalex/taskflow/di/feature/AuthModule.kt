package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.auth.AuthRepositoryImpl
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.single

val authModule = module {

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    viewModel { SignInViewModel(get(), get()) }

    viewModel { SignUpViewModel(get(), get()) }

}