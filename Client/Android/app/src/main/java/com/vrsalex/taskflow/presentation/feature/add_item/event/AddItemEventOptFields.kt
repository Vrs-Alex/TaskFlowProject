package com.vrsalex.taskflow.presentation.feature.add_item.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.input.SmallTextInput

@Composable
fun AddItemEventOptFields(
    state: AddItemEventContract.State,
    onAction: (AddItemEventContract.Action) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallTextInput(
            value = state.location ?: "",
            onValueChange = { onAction(AddItemEventContract.Action.LocationChanged(it)) },
            placeholder = stringResource(R.string.location)
        )
    }
}
