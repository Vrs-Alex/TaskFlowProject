package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.feature.workspace.area.toUiModel
import com.vrsalex.taskflow.presentation.feature.event.EventUiModel
import com.vrsalex.taskflow.presentation.navigation.bottom_sheet.item.ItemBottomSheetBaseContent
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun BottomSheetEventContent(
    eventUi: EventUiModel,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit,
) {

    ItemBottomSheetBaseContent(
        itemType = ItemCardType.Event,
        header = stringResource(R.string.event),
        title = eventUi.event.base.name,
        description = eventUi.event.base.description,
        area = eventUi.event.base.area?.toUiModel(),
        tags = eventUi.tags,
        synced = eventUi.event.isSynced,
        subline = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.surfaceElevated,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = com.vrsalex.uikit.R.drawable.time,
                        tint = AppTheme.colors.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = eventUi.dateString,
                        style = AppTheme.types.bodyMedium,
                        color = AppTheme.colors.onSurface,
                    )
                }
                eventUi.event.location?.let { location ->
                    HorizontalDivider(color = AppTheme.colors.outline)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppIcon(
                            icon = com.vrsalex.uikit.R.drawable.location,
                            tint = AppTheme.colors.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = location,
                            style = AppTheme.types.bodyMedium,
                            color = AppTheme.colors.onSurface,
                        )
                    }
                }
            }
        },
        onClose = onClose,
        onDelete = onDelete,
        onArchive = onArchive
    )
}