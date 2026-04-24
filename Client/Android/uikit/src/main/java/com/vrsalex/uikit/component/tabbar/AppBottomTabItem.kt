package com.vrsalex.uikit.component.tabbar

import androidx.annotation.StringRes

data class AppBottomTabItem<T: Any>(
    val payload: T,
    val unSelectedIcon: Int,
    val selectedIcon: Int,
    @param:StringRes val titleId: Int
)
