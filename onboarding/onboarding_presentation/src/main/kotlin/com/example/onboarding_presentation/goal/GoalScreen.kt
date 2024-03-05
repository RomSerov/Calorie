package com.example.onboarding_presentation.goal

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
import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.GoalType
import com.example.core.util.UIEvent
import com.example.core_ui.LocalSpacing
import com.example.onboarding_presentation.components.OnboardingActionButton
import com.example.onboarding_presentation.components.OnboardingSelectButton
import com.example.ui.CalorieTheme

@Composable
fun GoalScreen(
    viewModel: GoalViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val goalState by viewModel.selectedGoal.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                is UIEvent.Success -> onNextClick()
                else -> Unit
            }
        }
    }

    GoalScreenRender(
        goalState = goalState,
        onClickSelect = viewModel::onGoalTypeSelect,
        onClickNext = viewModel::onNextClick
    )
}

@Composable
private fun GoalScreenRender(
    goalState: GoalType,
    onClickNext: () -> Unit,
    onClickSelect: (GoalType) -> Unit
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
                text = stringResource(id = R.string.lose_keep_or_gain_weight),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row {
                OnboardingSelectButton(
                    text = stringResource(id = R.string.lose),
                    isSelected = goalState is GoalType.LoseWeight,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    onClick = {
                        onClickSelect(GoalType.LoseWeight)
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                OnboardingSelectButton(
                    text = stringResource(id = R.string.keep),
                    isSelected = goalState is GoalType.KeepWeight,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    onClick = {
                        onClickSelect(GoalType.KeepWeight)
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                OnboardingSelectButton(
                    text = stringResource(id = R.string.gain),
                    isSelected = goalState is GoalType.GainWeight,
                    color = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Color.White,
                    onClick = {
                        onClickSelect(GoalType.GainWeight)
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }
        OnboardingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next),
            onClick = { onClickNext() }
        )
    }
}

@Preview
@Composable
private fun GoalScreenRenderPreview() {
    CalorieTheme {
        GoalScreenRender(
            goalState = GoalType.KeepWeight,
            onClickNext = {},
            onClickSelect = {}
        )
    }
}