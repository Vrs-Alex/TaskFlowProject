package com.vrsalex.taskflow.presentation.navigation

import kotlinx.serialization.Serializable


// Onboarding
@Serializable
data object OnBoardingDestination

// Auth
@Serializable
data object AuthGraph

@Serializable
data object LoginDestination

@Serializable
data object RegisterDestination

@Serializable
data object ForgotPasswordDestination


// App
@Serializable
data object MainGraph

interface BottomNavDestination

@Serializable
data object InboxDestination: BottomNavDestination

@Serializable
data object CalendarDestination: BottomNavDestination

@Serializable
data object BrowseDestination: BottomNavDestination

@Serializable
data object ProfileDestination: BottomNavDestination