package com.vrsalex.taskflow.presentation.feature.shell

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vrsalex.taskflow.presentation.feature.calendar.CalendarScreen
import com.vrsalex.taskflow.presentation.feature.inbox.InboxScreen
import com.vrsalex.taskflow.presentation.feature.profile.ProfileScreen
import com.vrsalex.taskflow.presentation.navigation.BrowseDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.taskflow.presentation.navigation.bottom.bottomTabs


private fun NavDestination?.tabIndex(): Int =
    bottomTabs.indexOfFirst { this?.hasRoute(it.payload::class) == true }

private val tabSlideIn: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    val from = initialState.destination.tabIndex()
    val to = targetState.destination.tabIndex()
    slideInHorizontally(tween(200)) { if (to >= from) it else -it }
}

private val tabSlideOut: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    val from = initialState.destination.tabIndex()
    val to = targetState.destination.tabIndex()
    slideOutHorizontally(tween(200)) { if (to >= from) -it else it }
}


@Composable
fun ShellNavHost(innerNavController: NavHostController, modifier: Modifier) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = innerNavController,
        startDestination = InboxDestination,
        enterTransition = tabSlideIn,
        exitTransition = tabSlideOut,
        popEnterTransition = tabSlideIn,
        popExitTransition = tabSlideOut,
    ) {
        composable<InboxDestination> { InboxScreen() }
        composable<CalendarDestination> { CalendarScreen() }
        composable<BrowseDestination> {  }
        composable<ProfileDestination> { ProfileScreen() }
    }

}