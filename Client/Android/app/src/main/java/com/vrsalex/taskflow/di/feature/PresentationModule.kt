package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.common.message.AppMessenger
import com.vrsalex.taskflow.presentation.common.message.AppMessengerImpl
import com.vrsalex.taskflow.presentation.feature.add_note.AddNoteViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import com.vrsalex.taskflow.presentation.feature.inbox.InboxViewModel
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    single<AppMessenger> { AppMessengerImpl() }

    viewModelOf(::OnBoardingViewModel)

    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)

    viewModelOf(::AddNoteViewModel)
    viewModelOf(::InboxViewModel)
}