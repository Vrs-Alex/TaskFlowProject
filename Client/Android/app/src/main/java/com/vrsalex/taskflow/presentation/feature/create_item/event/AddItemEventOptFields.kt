package com.vrsalex.taskflow.presentation.feature.create_item.event

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.extension.getItemTypeColor
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.component.time.AppDateTimePicker
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun AddItemEventOptFields(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit
) {
    val eventData = state.subItemData as? AddItemContract.SubItemData.Event

    AnimatedVisibility(
        visible = eventData != null,
        enter = fadeIn(tween(200)) + expandVertically(tween(250)),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
    ) {
        val data = eventData ?: return@AnimatedVisibility

        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallTextInput(
                value = data.location ?: "",
                onValueChange = {
                    onAction(AddItemContract.Action.EventAction.LocationChanged(it))
                },
                placeholder = stringResource(R.string.location)
            )
        }
    }
}