package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.common.message.AppMessenger
import com.vrsalex.taskflow.presentation.common.message.AppMessengerImpl
import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    single<AppMessenger> { AppMessengerImpl() }

    viewModelOf(::OnBoardingViewModel)

    // ⚠️ требуют биндинга AuthRepository (auth data-слой ещё не реализован)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
}