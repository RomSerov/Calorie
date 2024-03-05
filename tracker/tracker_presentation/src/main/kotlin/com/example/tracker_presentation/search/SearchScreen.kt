package com.example.tracker_presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.R
import com.example.core.util.UIEvent
import com.example.core_ui.LocalSpacing
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_presentation.search.components.SearchTextField
import com.example.tracker_presentation.search.components.TrackableFoodItem
import com.example.ui.CalorieTheme
import java.time.LocalDate


@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                    keyboardController?.hide()
                }

                is UIEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    SearchScreenRender(
        state = state,
        mealName = mealName,
        dayOfMonth = dayOfMonth,
        month = month,
        year = year,
        onValueChange = {
            viewModel.onEvent(SearchEvent.OnQueryChange(it))
        },
        onSearch = {
            keyboardController?.hide()
            viewModel.onEvent(SearchEvent.OnSearch)
        },
        onFocusChanged = {
            viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
        },
        onTrackableFoodItemClick = {
            viewModel.onEvent(SearchEvent.OnToggleTrackableFood(it))
        },
        onAmountChange = { food, amount ->
            viewModel.onEvent(
                SearchEvent.OnAmountForFoodChange(
                    food = food,
                    amount = amount
                )
            )
        },
        onTrack = { food, mealType, date ->
            keyboardController?.hide()
            viewModel.onEvent(
                SearchEvent.OnTrackFoodClick(
                    food = food,
                    mealType = mealType,
                    date = date
                )
            )
        }
    )
}

@Composable
private fun SearchScreenRender(
    state: SearchState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onTrackableFoodItemClick: (TrackableFood) -> Unit,
    onAmountChange: (TrackableFood, String) -> Unit,
    onTrack: (TrackableFood, MealType, LocalDate) -> Unit
) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        SearchTextField(
            text = state.query,
            onValueChange = onValueChange,
            shouldShowHint = state.isHintVisible,
            onSearch = onSearch,
            onFocusChanged = onFocusChanged
        )
        Spacer(modifier = Modifier.height(spacing.spaceMedium))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.trackableFood) { food ->
                TrackableFoodItem(
                    trackableFoodUiState = food,
                    onClick = {
                        onTrackableFoodItemClick(food.food)
                    },
                    onAmountChange = {
                        onAmountChange(food.food, it)
                    },
                    onTrack = {
                        onTrack(
                            food.food,
                            MealType.fromString(mealName),
                            LocalDate.of(year, month, dayOfMonth)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isSearching -> CircularProgressIndicator()
            state.trackableFood.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenRenderPreview() {
    CalorieTheme {
        SearchScreenRender(
            state = SearchState(
                query = "",
                isHintVisible = true,
                trackableFood = listOf(
                    TrackableFoodUiState(
                        food = TrackableFood(
                            name = "test1",
                            imageUrl = "",
                            carbsPer100g = 100,
                            caloriesPer100g = 100,
                            proteinPer100g = 100,
                            fatPer100g = 100
                        )
                    ),
                    TrackableFoodUiState(
                        food = TrackableFood(
                            name = "test2",
                            imageUrl = "",
                            carbsPer100g = 100,
                            caloriesPer100g = 100,
                            proteinPer100g = 100,
                            fatPer100g = 100
                        ),
                        isExpanded = true,
                        amount = "test"
                    )
                )
            ),
            mealName = "Test",
            dayOfMonth = 1,
            month = 1,
            year = 2024,
            onValueChange = {},
            onSearch = {},
            onFocusChanged = {},
            onTrackableFoodItemClick = {},
            onAmountChange = { _, _ -> },
            onTrack = { _, _, _ -> }
        )
    }
}