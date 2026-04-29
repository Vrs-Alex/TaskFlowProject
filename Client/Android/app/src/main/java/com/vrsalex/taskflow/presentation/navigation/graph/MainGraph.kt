package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContent
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemViewModel
import com.vrsalex.taskflow.presentation.feature.home.HomeScreen
import com.vrsalex.taskflow.presentation.feature.profile.ProfileScreen
import com.vrsalex.taskflow.presentation.navigation.ArchiveDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.HomeDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.taskflow.presentation.navigation.bottom.bottomTabs
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.tabbar.AppBottomTabBar
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable<MainGraph> {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination

        val addViewModel: AddItemViewModel = koinViewModel()

        Box(Modifier.fillMaxSize()) {

            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startDestination = HomeDestination
            ) {
                composable<HomeDestination> {
                    HomeScreen()
                }
                composable<ArchiveDestination> {

                }
                composable<CalendarDestination> {

                }
                composable<ProfileDestination> {
                    ProfileScreen()
                }
            }

            AppBottomTabBar(
                modifier = Modifier.align(Alignment.BottomCenter),
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

            AnimatedVisibility(
                currentDestination?.hasRoute<HomeDestination>() == true,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(bottom = 80.dp, end = 16.dp)
                    .clip(AppTheme.shapes.large)
            ) {
                FloatingActionButton(
                    onClick = { addViewModel.onVisibilityChanged(true) },
                    containerColor = AppTheme.colors.primary,
                    shape = AppTheme.shapes.large
                ) {
                    AppIcon(
                        icon = R.drawable.pen,
                        tint = AppTheme.colors.onPrimary
                    )
                }
            }

            AddItemContent(
                addViewModel
            ){
                addViewModel.onVisibilityChanged(false)
            }

        }
    }
}