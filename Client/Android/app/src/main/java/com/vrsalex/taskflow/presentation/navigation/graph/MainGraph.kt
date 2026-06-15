package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.navigation.BrowseDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.taskflow.presentation.navigation.bottom.bottomTabs
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.tabbar.AppBottomTabBar
import com.vrsalex.uikit.theme.AppTheme

fun NavGraphBuilder.mainGraph(){

    composable<MainGraph> {
        val navController = rememberNavController()
        val backStack by navController.currentBackStackEntryAsState()
        val currentDestination = backStack?.destination

        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = InboxDestination
            ) {

                composable<InboxDestination> {  }

                composable<CalendarDestination> {  }

                composable<BrowseDestination> {  }

                composable<ProfileDestination> {  }

            }
            AnimatedVisibility(
                visible = bottomTabs.any { currentDestination?.hasRoute(it.payload::class) == true },
                enter = slideInVertically { it },
                exit = slideOutVertically { -it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Box() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
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
                            navController.navigate(payload) {
                                popUpTo(InboxDestination) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = currentDestination?.hasRoute<InboxDestination>() == true ||
                        currentDestination?.hasRoute<CalendarDestination>() == true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(bottom = 78.dp, end = 16.dp)
                    .clip(AppTheme.shapes.large)
            ) {
                FloatingActionButton(
                    onClick = {  },
                    containerColor = AppTheme.colors.primary,
                    shape = AppTheme.shapes.large
                ) {
                    AppIcon(icon = R.drawable.pen, tint = AppTheme.colors.onPrimary)
                }
            }
        }
    }

}