package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.feature.inbox.elements.EmptyInbox
import com.vrsalex.taskflow.presentation.model.note.NoteUiModel
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
import com.vrsalex.uikit.component.background.AppBlurBackground
import com.vrsalex.taskflow.presentation.common.card.NoteCard
import com.vrsalex.taskflow.presentation.common.card.TaskCard
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.menu.ItemsMenu
import com.vrsalex.uikit.theme.AppTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.koin.androidx.compose.koinViewModel

@Composable
fun InboxScreen(viewModel: InboxViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    InboxContent(state, viewModel::onAction)
}

@Composable
private fun InboxContent(
    state: InboxContract.State,
    onAction: (InboxContract.Action) -> Unit,
) {
    val hazeState = rememberHazeState()

    Box() {
        LazyColumn(
            modifier = Modifier.fillMaxSize().hazeSource(hazeState),
            contentPadding = PaddingValues(start = 16.dp, top = 120.dp, end = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            if (!state.isLoading && state.isEmpty) {
                item(contentType = "Empty") { EmptyInbox() }
            }

            items(state.tasks, key = { it.id }, contentType = { "Task" }) { task ->
                TaskRow(task, onAction, Modifier.animateItem())
            }

            items(state.notes, key = { it.id }, contentType = { "Note" }) { note ->
                NoteRow(note, onAction, Modifier.animateItem())
            }
        }


        AppBlurBackground(
            hazeState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tab_inbox),
                    style = AppTheme.types.headline,
                    color = AppTheme.colors.onSurface,
                )
                Spacer(Modifier.weight(1f))
                ItemsMenu(
                    options = InboxContract.SortedListBy.entries,
                    selected = state.currentSortedListBy,
                    label = { stringResource(it.title) },
                    onSelect = { onAction(InboxContract.Action.OnChangeSorted(it)) }
                ) {
                    AppIcon(
                        icon = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.filter),
                        tint = AppTheme.colors.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    ui: TaskUiModel,
    onAction: (InboxContract.Action) -> Unit,
    modifier: Modifier
) {
    TaskCard(
        ui = ui,
        onCheckedChange = { onAction(InboxContract.Action.TaskCheckedChange(ui.id, ui.isCompleted)) },
        onClick = { onAction(InboxContract.Action.ItemClicked(ui.id)) },
        modifier = modifier
    )
}

@Composable
private fun NoteRow(
    ui: NoteUiModel,
    onAction: (InboxContract.Action) -> Unit,
    modifier: Modifier
) {
    NoteCard(
        ui = ui,
        onClick = { onAction(InboxContract.Action.ItemClicked(ui.id)) },
        modifier = modifier
    )
}

