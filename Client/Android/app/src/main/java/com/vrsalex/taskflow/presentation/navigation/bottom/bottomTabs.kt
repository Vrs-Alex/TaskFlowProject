package com.vrsalex.taskflow.presentation.navigation.bottom

import com.vrsalex.taskflow.presentation.navigation.BottomNavDestination
import com.vrsalex.taskflow.presentation.navigation.BrowseDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.tabbar.AppBottomTabItem


val bottomTabs = listOf<AppBottomTabItem<BottomNavDestination>>(
    AppBottomTabItem(
        payload = InboxDestination,
        unSelectedIcon = R.drawable.tab_inbox,
        selectedIcon = R.drawable.tab_inbox_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_inbox
    ),
    AppBottomTabItem(
        payload = CalendarDestination,
        unSelectedIcon = R.drawable.tab_calendar,
        selectedIcon = R.drawable.tab_calendar_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_calendar
    ),
    AppBottomTabItem(
        payload = BrowseDestination,
        unSelectedIcon = R.drawable.tab_browse,
        selectedIcon = R.drawable.tab_browse_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_browse
    ),
    AppBottomTabItem(
        payload = ProfileDestination,
        unSelectedIcon = R.drawable.tab_profile,
        selectedIcon = R.drawable.tab_profile_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_profile
    )
)