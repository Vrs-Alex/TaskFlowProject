package com.vrsalex.taskflow.presentation.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    ProfileContract(state, viewModel::onAction)
}

@Composable
private fun ProfileContract(
    state: ProfileContract.State,
    onAction: (ProfileContract.Action) -> Unit
) {



}