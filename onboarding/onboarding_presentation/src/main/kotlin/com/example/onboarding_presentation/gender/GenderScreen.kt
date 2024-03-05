package com.example.onboarding_presentation.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.R
import com.example.core.domain.model.Gender
import com.example.core.util.UIEvent
import com.example.core_ui.LocalSpacing
import com.example.onboarding_presentation.components.OnboardingActionButton
import com.example.onboarding_presentation.components.OnboardingSelectButton
import com.example.ui.CalorieTheme

@Composable
fun GenderScreen(
    viewModel: GenderViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val select by viewModel.gender.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UIEvent.Success -> onNextClick()
                else -> Unit
            }
        }
    }

    GenderScreenRender(
        select = select,
        onClickNext = {
            viewModel.onNextClick()
        },
        onClickSelect = {
            viewModel.onGenderClick(gender = it)
        }
    )
}

@Composable
private fun GenderScreenRender(
    select: Gender,
    onClickNext: () -> Unit,
    onClickSelect: (Gender) -> Unit
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
                text = stringResource(id = R.string.whats_your_gender),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            Row {
                OnboardingSelectButton(
                    text = stringResource(id = R.string.male),
                    isSelected = select is Gender.Male,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    textStyle = MaterialTheme.typography.titleMedium,
                    onClick = {
                        onClickSelect(Gender.Male)
                    }
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                OnboardingSelectButton(
                    text = stringResource(id = R.string.female),
                    isSelected = select is Gender.Female,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    textStyle = MaterialTheme.typography.titleMedium,
                    onClick = {
                        onClickSelect(Gender.Female)
                    }
                )
            }
        }
        OnboardingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next),
            onClick = {
                onClickNext()
            }
        )
    }
}

@Preview
@Composable
private fun GenderScreenRenderPreview() {
    CalorieTheme {
        GenderScreenRender(
            select = Gender.Male,
            onClickNext = {},
            onClickSelect = {}
        )
    }
}