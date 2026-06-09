package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event.EventDetailViewModel
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task.TaskDetailViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import com.vrsalex.taskflow.presentation.feature.calendar.CalendarViewModel
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemViewModel
import com.vrsalex.taskflow.presentation.feature.home.HomeViewModel
import com.vrsalex.taskflow.presentation.feature.inbox.InboxViewModel
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import com.vrsalex.taskflow.presentation.feature.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {

    // Bottom Sheet
    single { ItemBottomSheetRouter() }

    viewModel { params -> EventDetailViewModel(params.get(), get()) }
    viewModel { params -> TaskDetailViewModel(params.get(), get()) }
    viewModelOf(::AddItemViewModel)

    // Screens
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CalendarViewModel)
    viewModelOf(::InboxViewModel)
    viewModelOf(::ProfileViewModel)
}