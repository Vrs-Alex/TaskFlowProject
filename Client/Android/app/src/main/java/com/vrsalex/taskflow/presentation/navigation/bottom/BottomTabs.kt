package com.vrsalex.taskflow.presentation.navigation.bottom

import com.vrsalex.taskflow.presentation.navigation.ArchiveDestination
import com.vrsalex.taskflow.presentation.navigation.BottomTabDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.HomeDestination
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.tabbar.AppBottomTabItem

val bottomTabs = listOf<AppBottomTabItem<BottomTabDestination>>(
    AppBottomTabItem(
        payload = HomeDestination,
        unSelectedIcon = R.drawable.tab_today,
        selectedIcon = R.drawable.tab_today_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_today
    ),
    AppBottomTabItem(
        payload = ArchiveDestination,
        unSelectedIcon = R.drawable.tab_archive,
        selectedIcon = R.drawable.tab_archive_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_archive
    ),
    AppBottomTabItem(
        payload = CalendarDestination,
        unSelectedIcon = R.drawable.tab_calendar,
        selectedIcon = R.drawable.tab_calendar_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_calendar
    ),
    AppBottomTabItem(
        payload = ProfileDestination,
        unSelectedIcon = R.drawable.tab_profile,
        selectedIcon = R.drawable.tab_profile_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_profile
    )
)