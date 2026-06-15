package com.vrsalex.taskflow.presentation.navigation

import kotlinx.serialization.Serializable


// Onboarding
@Serializable
data object OnBoardingScreen

// Auth
@Serializable
data object AuthGraph

@Serializable
data object LoginDestination

@Serializable
data object RegisterDestination

@Serializable
data object ForgotPassword


// App
@Serializable
data object MainGraph

@Serializable
data object InboxDestination

@Serializable
data object CalendarDestination

@Serializable
data object BrowseDestination

@Serializable
data object ProfileDestination