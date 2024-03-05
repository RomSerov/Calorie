package com.example.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core_ui.LocalSpacing
import com.example.tracker_presentation.tracker_overview.components.DaySelector
import com.example.tracker_presentation.tracker_overview.components.ExpandableMeal
import com.example.tracker_presentation.tracker_overview.components.NutrientsHeader
import com.example.ui.CalorieTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.example.tracker_presentation.tracker_overview.components.AddButton
import com.example.tracker_presentation.tracker_overview.components.TrackedFoodItem
import com.example.core.R

@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    TrackerOverviewScreenRender(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToSearch = onNavigateToSearch
    )
}

@Composable
private fun TrackerOverviewScreenRender(
    state: TrackerOverviewState,
    onEvent: (TrackerOverviewEvent) -> Unit,
    onNavigateToSearch: (String, Int, Int, Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        item {
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            DaySelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium),
                date = state.date,
                onPreviousDayClick = {
                    onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    onEvent(TrackerOverviewEvent.OnNextDayClick)
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        items(state.meals) { meal ->
            ExpandableMeal(
                meal = meal,
                onToggleClick = {
                    onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
                    ) {
                        val foods = state.trackedFoods.filter {
                            it.mealType == meal.mealType
                        }
                        foods.forEach { food ->
                            TrackedFoodItem(
                                trackedFood = food,
                                onDeleteClick = {
                                   onEvent(TrackerOverviewEvent.OnDeleteTrackedFoodClick(food))
                                }
                            )
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        }
                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context)
                            ),
                            onClick = {
                                onNavigateToSearch(
                                    meal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TrackerOverviewScreenRenderPreview() {
    CalorieTheme {
        TrackerOverviewScreenRender(
            state = TrackerOverviewState(),
            onEvent = {},
            onNavigateToSearch = { _, _, _, _ ->}
        )
    }
}