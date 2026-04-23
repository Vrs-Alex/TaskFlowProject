package com.vrsalex.taskflow.presentation.feature.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import com.vrsalex.uikit.component.button.AppOutlinedButton
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.AppPasswordInput
import com.vrsalex.uikit.component.input.AppTextInput
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    viewModel: SignInViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.channel.collect {
            when(it) {
                SignInContract.Effect.OnSignIn -> onSignIn()
                SignInContract.Effect.OnSignUp -> onSignUp()
            }
        }
    }

        SignInContent(state, viewModel::onEvent)
}


@Composable
private fun SignInContent(
    state: SignInContract.State,
    event: (SignInContract.Event) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .padding(horizontal = 20.dp).padding(top = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = AppTheme.types.displayLarge,
            color = AppTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.sign_in_description),
            style = AppTheme.types.body,
            color = AppTheme.colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AppTextInput(
                value = state.identity,
                onValueChange = {
                    event(SignInContract.Event.FieldChanged(SignInContract.Field.Identity, it))
                },
                label = stringResource(R.string.email_or_username),
                placeholder = "example@example.com",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            AppPasswordInput(
                value = state.password,
                onValueChange = {
                    event(SignInContract.Event.FieldChanged(SignInContract.Field.Password, it))
                },
                label = stringResource(R.string.password),
                placeholder = "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = { if(state.isEnabled) event(SignInContract.Event.Submit) }
                )
            )

        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.no_account),
            style = AppTheme.types.label,
            color = AppTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable(
                interactionSource = null,
                indication = null,
                onClick = { event(SignInContract.Event.SignUp) }
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        AppButton(
            onClick = { event(SignInContract.Event.Submit) },
            text = stringResource(R.string.sign_in),
            state = AppButtonState.Large,
            enabled = state.isEnabled,
            modifier = Modifier.imePadding()
        )
        Spacer(modifier = Modifier.height(24.dp))

    }

}