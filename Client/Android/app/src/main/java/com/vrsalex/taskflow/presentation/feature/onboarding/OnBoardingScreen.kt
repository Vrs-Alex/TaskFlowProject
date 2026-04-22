package com.vrsalex.taskflow.presentation.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnBoardingScreen(
    onNext: () -> Unit,
    viewModel: OnBoardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.channel.collect { onNext() }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        OnBoardingPage(
            page = state.pages[page],
            onNext = {
                scope.launch {
                    if (page == state.pages.lastIndex) {
                        onNext()
                    } else {
                        pagerState.animateScrollToPage(page + 1)
                    }
                }
            }
        )
    }

}

@Composable
private fun OnBoardingPage(page: OnBoardingContract.Page, onNext: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(page.imageId)
    )

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxWidth(0.8f).height(300.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(page.titleId),
                style = AppTheme.types.headline,
                color = AppTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(page.descriptionId),
                style = AppTheme.types.body,
                color = AppTheme.colors.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
        }

        AppButton(
            onClick = { onNext() },
            text = stringResource(R.string.next),
            state = AppButtonState.Large,
            modifier = Modifier.fillMaxWidth(0.85f)
        )
    }

}