package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.feature.calendar.CalendarScreen
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContent
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemViewModel
import com.vrsalex.taskflow.presentation.feature.home.HomeScreen
import com.vrsalex.taskflow.presentation.feature.inbox.InboxScreen
import com.vrsalex.taskflow.presentation.feature.profile.ProfileScreen
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.TodayDestination
import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.taskflow.presentation.navigation.bottom.bottomTabs
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.tabbar.AppBottomTabBar
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel


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

fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable<MainGraph> {
        val innerNavController = rememberNavController()
        val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination

        val addViewModel: AddItemViewModel = koinViewModel()
        var isAddItemVisible by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxSize()) {

            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = innerNavController,
                startDestination = InboxDestination,
                enterTransition = tabSlideIn,
                exitTransition = tabSlideOut,
                popEnterTransition = tabSlideIn,
                popExitTransition = tabSlideOut,
            ) {
                composable<InboxDestination> { InboxScreen() }
                composable<TodayDestination> { HomeScreen() }
                composable<CalendarDestination> {  }
                composable<ProfileDestination> { ProfileScreen() }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, AppTheme.colors.background)
                        )
                    )
            )

            AppBottomTabBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                tabs = bottomTabs,
                isSelected = { payload ->
                    currentDestination?.hierarchy?.any { it.hasRoute(payload::class) } == true
                },
                onSelected = { payload ->
                    innerNavController.navigate(payload) {
                        popUpTo(InboxDestination) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            AnimatedVisibility(
                currentDestination?.hasRoute<InboxDestination>() == true ||
                        currentDestination?.hasRoute<TodayDestination>() == true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(bottom = 78.dp, end = 16.dp)
                    .clip(AppTheme.shapes.large)
            ) {
                FloatingActionButton(
                    onClick = { isAddItemVisible = true },
                    containerColor = AppTheme.colors.primary,
                    shape = AppTheme.shapes.large
                ) {
                    AppIcon(icon = R.drawable.pen, tint = AppTheme.colors.onPrimary)
                }
            }

            AddItemContent(
                viewModel = addViewModel,
                isVisible = isAddItemVisible,
                onClose = { isAddItemVisible = false }
            )
        }
    }
}
