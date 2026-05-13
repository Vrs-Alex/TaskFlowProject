package com.vrsalex.uikit.component.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme

@Composable
  fun <T> SortMenu(
    options: List<T>,
    selected: T,
    label: @Composable (T) -> String,
    onSelect: (T) -> Unit,
    anchor: @Composable () -> Unit,
  ) {
      var expanded by remember { mutableStateOf(false) }
      Box {
          Box(Modifier.clickable { expanded = true }) { anchor() }
          DropdownMenu(
              expanded = expanded,
              containerColor = AppTheme.colors.surfaceElevated,
              shape = AppTheme.shapes.medium,
              shadowElevation = 4.dp,
              onDismissRequest = { expanded = false }
          ) {
              options.forEach { option ->
                  DropdownMenuItem(
                      text = {
                          Text(
                              text = label(option),
                              style = AppTheme.types.body,
                              color = AppTheme.colors.onSurfaceVariant
                          )
                      },
                      trailingIcon = {
                          if (option == selected)
                              AppIcon(icon = R.drawable.check, tint = AppTheme.colors.primary)
                      },
                      onClick = { onSelect(option); expanded = false }
                  )
              }
          }
      }
  }
