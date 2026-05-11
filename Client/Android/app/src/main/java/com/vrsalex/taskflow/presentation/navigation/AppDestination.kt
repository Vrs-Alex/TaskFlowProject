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

interface BottomTabDestination

@Serializable
data object HomeDestination : BottomTabDestination

@Serializable
data object InboxDestination : BottomTabDestination

@Serializable
data object CalendarDestination : BottomTabDestination

@Serializable
data object ProfileDestination : BottomTabDestination
