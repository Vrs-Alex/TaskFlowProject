package com.vrsalex.taskflow.presentation.navigation.bottom

import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.BottomTabDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.TodayDestination
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.tabbar.AppBottomTabItem

val bottomTabs = listOf<AppBottomTabItem<BottomTabDestination>>(
    AppBottomTabItem(
        payload = InboxDestination,
        unSelectedIcon = R.drawable.tab_inbox,
        selectedIcon = R.drawable.tab_inbox_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_inbox
    ),
    AppBottomTabItem(
        payload = TodayDestination,
        unSelectedIcon = R.drawable.tab_today,
        selectedIcon = R.drawable.tab_today_fill,
        titleId = com.vrsalex.taskflow.R.string.tab_today
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
