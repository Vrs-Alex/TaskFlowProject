package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import com.vrsalex.taskflow.presentation.feature.home.HomeViewModel
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemViewModel
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.event.EventDetailViewModel
import com.vrsalex.taskflow.presentation.feature.archive.ArchiveViewModel
import com.vrsalex.taskflow.presentation.feature.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    // Bottom Sheet

    single { ItemBottomSheetRouter() }

    viewModel { params -> EventDetailViewModel(params.get(),  get()) }

    viewModel { AddItemViewModel(get(), get(), get()) }


    // Screens

    viewModel { OnBoardingViewModel(get()) }

    viewModel { SignInViewModel(get(), get()) }

    viewModel { SignUpViewModel(get(), get()) }

    viewModel { HomeViewModel(get(), get(), get()) }

    viewModel { ArchiveViewModel(get(), get()) }

    viewModel { ProfileViewModel(get()) }

}