package com.example.tracker_presentation.tracker_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.preferences.Preferences
import com.example.core.util.UIEvent
import com.example.tracker_domain.usecase.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    private val preferences: Preferences,
    private val trackerUseCases: TrackerUseCases,
) : ViewModel() {

    private val _state: MutableStateFlow<TrackerOverviewState> =
        MutableStateFlow(TrackerOverviewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getFoodsForDateJob: Job? = null

    init {
        refreshFoods()
        preferences.saveShouldShowOnboarding(shouldShow = false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    trackerUseCases.deleteTrackedFood(event.trackedFood)
                    refreshFoods()
                }
            }

            TrackerOverviewEvent.OnNextDayClick -> {
                _state.update {
                    it.copy(
                        date = state.value.date.plusDays(1)
                    )
                }
                refreshFoods()
            }

            TrackerOverviewEvent.OnPreviousDayClick -> {
                _state.update {
                    it.copy(
                        date = state.value.date.minusDays(1)
                    )
                }
                refreshFoods()
            }

            is TrackerOverviewEvent.OnToggleMealClick -> {
                _state.update {
                    it.copy(
                        meals = state.value.meals.map { meal ->
                            if (meal.name == event.meal.name) {
                                meal.copy(isExpanded = !meal.isExpanded)
                            } else meal
                        }
                    )
                }
            }
        }
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases
            .getFoodsForDate(state.value.date)
            .onEach { foods ->
                val nutrientsResult = trackerUseCases.calculateMealNutrients(foods)
                _state.update {
                    it.copy(
                        totalCarbs = nutrientsResult.totalCarbs,
                        totalProtein = nutrientsResult.totalProtein,
                        totalFat = nutrientsResult.totalFat,
                        totalCalories = nutrientsResult.totalCalories,
                        carbsGoal = nutrientsResult.carbsGoal,
                        proteinGoal = nutrientsResult.proteinGoal,
                        fatGoal = nutrientsResult.fatGoal,
                        caloriesGoal = nutrientsResult.caloriesGoal,
                        trackedFoods = foods,
                        meals = state.value.meals.map { meal ->
                            val nutrientsForMeal =
                                nutrientsResult.mealNutrients[meal.mealType]
                                    ?: return@map meal.copy(
                                        carbs = 0,
                                        protein = 0,
                                        fat = 0,
                                        calories = 0
                                    )
                            meal.copy(
                                carbs = nutrientsForMeal.carbs,
                                protein = nutrientsForMeal.protein,
                                fat = nutrientsForMeal.fat,
                                calories = nutrientsForMeal.calories
                            )
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}