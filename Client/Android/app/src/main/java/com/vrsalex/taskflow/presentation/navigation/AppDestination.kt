package com.vrsalex.taskflow.presentation.navigation

import kotlinx.serialization.Serializable

// OnBoarding
@Serializable
data object OnBoardingDestination

// Auth
@Serializable
data object AuthGraph

@Serializable
data object SignInDestination

@Serializable
data object SignUpDestination

// Main
@Serializable
data object MainGraph

@Serializable
data object HomeDestination

@Serializable
data object ArchiveDestination

@Serializable
data object CalendarDestination

@Serializable
data object ProfileDestination
