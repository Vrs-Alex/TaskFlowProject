package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.task.Task
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import com.vrsalex.uikit.component.background.AppBackground
import com.vrsalex.uikit.component.card.NoteCard
import com.vrsalex.uikit.component.card.TaskCard
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

            items(state.tasks, key = { it.note.syncModel.id }, contentType = { "Task" }) { task ->
                TaskRow(task, onAction)
            }

            items(state.notes, key = { it.syncModel.id }, contentType = { "Note" }) { note ->
                NoteRow(note, onAction)
            }
        }
        AppBackground(
            hazeState,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Входящие",
                    style = AppTheme.types.headline,
                    color = AppTheme.colors.onSurface,
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.filter),
                        contentDescription = null,
                        tint = AppTheme.colors.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(task: Task, onAction: (InboxContract.Action) -> Unit) {
    val note = task.note
    TaskCard(
        title = note.name.value,
        dueDate = null,
        time = task.dueTime?.toString(),
        isCompleted = task.isCompleted,
        areaName = note.area?.name?.value,
        areaColor = note.area?.color?.value?.toComposeColor(),
        tags = note.tags.map { it.name.value to it.color.value.toComposeColor() },
        synced = note.syncModel.isSynced,
        onCheckedChange = { onAction(InboxContract.Action.TaskCheckedChange(task)) },
        onClick = { onAction(InboxContract.Action.ItemClicked(note.syncModel.id)) },
    )
}

@Composable
private fun NoteRow(note: Note, onAction: (InboxContract.Action) -> Unit) {
    NoteCard(
        title = note.name.value,
        description = note.description?.value,
        areaName = note.area?.name?.value,
        areaColor = note.area?.color?.value?.toComposeColor(),
        tags = note.tags.map { it.name.value to it.color.value.toComposeColor() },
        synced = note.syncModel.isSynced,
        onClick = { onAction(InboxContract.Action.ItemClicked(note.syncModel.id)) },
    )
}

@Composable
private fun EmptyInbox() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Входящие пусты",
            style = AppTheme.types.title,
            color = AppTheme.colors.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Здесь появляется то, что вы быстро записали и ещё не разобрали",
            style = AppTheme.types.bodyMedium,
            color = AppTheme.colors.onSurfaceMuted,
            textAlign = TextAlign.Center,
        )
    }
}
