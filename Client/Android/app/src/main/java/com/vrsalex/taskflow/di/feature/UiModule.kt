package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInViewModel
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpViewModel
import com.vrsalex.taskflow.presentation.feature.home.HomeViewModel
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.add.AddBottomSheetViewModel
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.event.EventDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    // Bottom Sheet

    single { ItemBottomSheetRouter() }

    viewModel { params -> EventDetailViewModel(params.get(),  get()) }

    viewModel { AddBottomSheetViewModel(get()) }


    // Screens

    viewModel { OnBoardingViewModel(get()) }

    viewModel { SignInViewModel(get(), get()) }

    viewModel { SignUpViewModel(get(), get()) }

    viewModel { HomeViewModel(get(), get()) }

}