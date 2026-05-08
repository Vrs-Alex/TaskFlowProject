package com.vrsalex.taskflow.presentation.feature.create_item.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AddItemBaseFields(
    title: String,
    description: String,
    isActiveSubmit: Boolean,
    focusRequester: FocusRequester,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSave: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BasicTextField(
            value = title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            maxLines = 2,
            cursorBrush = SolidColor(AppTheme.colors.primary),
            textStyle = AppTheme.types.titleLarge.copy(color = AppTheme.colors.onSurface),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    if (title.isEmpty()) {
                        Text(
                            text = stringResource(R.string.item_create_example),
                            style = AppTheme.types.titleLarge,
                            color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                    innerTextField()
                }
            }
        )

        IconButton(
            onClick = onSave,
            shape = CircleShape,
            modifier = Modifier.height(42.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = AppTheme.colors.primary,
                disabledContainerColor = AppTheme.colors.primary.copy(alpha = 0.4f)
            ),
            enabled = isActiveSubmit
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.up),
                contentDescription = "Send",
                tint = AppTheme.colors.onPrimary
            )
        }
    }

    SmallTextInput(
        value = description,
        onValueChange = onDescriptionChanged,
        placeholder = stringResource(R.string.description)
    )
}
