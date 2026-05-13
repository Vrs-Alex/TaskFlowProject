package com.vrsalex.taskflow.presentation.common.extension

import androidx.compose.runtime.Composable
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.EventHue
import com.vrsalex.uikit.theme.TaskHue

@Composable
fun getItemTypeColor(type: ItemType) = when (type) {
    ItemType.NOTE -> AppTheme.typeColors.note
    ItemType.TASK -> AppTheme.typeColors.task
    ItemType.EVENT -> AppTheme.typeColors.event
}