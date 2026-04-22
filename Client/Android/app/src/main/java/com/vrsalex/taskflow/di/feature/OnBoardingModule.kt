package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.single
import kotlin.math.sin

val onBoardingModule = module {

    viewModel { OnBoardingViewModel() }

}