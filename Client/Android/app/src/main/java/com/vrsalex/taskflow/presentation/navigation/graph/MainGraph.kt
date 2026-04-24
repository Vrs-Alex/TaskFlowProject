package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.feature.home.HomeScreen
import com.vrsalex.taskflow.presentation.navigation.ArchiveDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.HomeDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.taskflow.presentation.navigation.bottom.bottomTabs
import com.vrsalex.uikit.component.tabbar.AppBottomTabBar
import com.vrsalex.uikit.theme.AppTheme

fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable<MainGraph> {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = AppTheme.colors.background,
            bottomBar = {
                AppBottomTabBar(
                    tabs = bottomTabs,
                    isSelected = { payload ->
                        currentDestination?.hierarchy?.any {
                            it.hasRoute(payload::class)
                        } == true
                    },
                    onSelected = { payload ->
                        navController.navigate(payload) {
                            popUpTo(HomeDestination) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { scaffoldPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeDestination
            ) {
                composable<HomeDestination> {
                    HomeScreen(scaffoldPadding)
                }
                composable<ArchiveDestination> {

                }
                composable<CalendarDestination> {

                }
                composable<ProfileDestination> {

                }
            }
        }
    }


}