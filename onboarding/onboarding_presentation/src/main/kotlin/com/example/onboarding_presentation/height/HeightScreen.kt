package com.example.onboarding_presentation.height

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.R
import com.example.core.util.UIEvent
import com.example.core_ui.LocalSpacing
import com.example.onboarding_presentation.age.AgeViewModel
import com.example.onboarding_presentation.components.OnboardingActionButton
import com.example.onboarding_presentation.components.UnitTextField
import com.example.ui.CalorieTheme

@Composable
fun HeightScreen(
    viewModel: HeightViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onNextClick: () -> Unit
) {
    val heightState by viewModel.height.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is UIEvent.Success -> onNextClick()
                is UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = it.message.asString(context)
                    )
                }

                else -> Unit
            }
        }
    }

    HeightScreenRender(
        height = heightState,
        onValueChange = viewModel::onHeightEnter,
        onClickNext = viewModel::onNextClick
    )
}

@Composable
private fun HeightScreenRender(
    height: String,
    onValueChange: (String) -> Unit,
    onClickNext: () -> Unit
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.whats_your_height),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = height,
                unit = stringResource(id = R.string.cm),
                onValueChange = onValueChange
            )
        }
        OnboardingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next),
            onClick = onClickNext
        )
    }
}

@Preview
@Composable
private fun HeightScreenRenderPreview() {
    CalorieTheme {
        HeightScreenRender(
            height = "180",
            onValueChange = {},
            onClickNext = {}
        )
    }
}