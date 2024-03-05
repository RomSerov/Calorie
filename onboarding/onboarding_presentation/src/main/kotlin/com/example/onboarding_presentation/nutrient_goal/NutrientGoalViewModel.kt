package com.example.onboarding_presentation.nutrient_goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.usecase.FilterOutDigits
import com.example.core.util.UIEvent
import com.example.onboarding_domain.usecase.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits,
    private val validateNutrients: ValidateNutrients
) : ViewModel() {

    private val _nutrientsGoal: MutableStateFlow<NutrientGoalState> =
        MutableStateFlow(NutrientGoalState())
    val nutrientsGoal = _nutrientsGoal.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: NutrientGoalEvent) {
        when (event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                _nutrientsGoal.update {
                    it.copy(carbsRatio = filterOutDigits(event.ratio))
                }
            }

            is NutrientGoalEvent.OnProteinRatioEnter -> {
                _nutrientsGoal.update {
                    it.copy(proteinRatio = filterOutDigits(event.ratio))
                }
            }

            is NutrientGoalEvent.OnFatRatioEnter -> {
                _nutrientsGoal.update {
                    it.copy(fatRatio = filterOutDigits(event.ratio))
                }
            }

            is NutrientGoalEvent.OnNextClick -> {
                val result = validateNutrients(
                    carbsRatioText = nutrientsGoal.value.carbsRatio,
                    proteinRatioText = nutrientsGoal.value.proteinRatio,
                    fatRatioText = nutrientsGoal.value.fatRatio
                )
                when (result) {
                    is ValidateNutrients.Result.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)
                        viewModelScope.launch {
                            _uiEvent.send(UIEvent.Success)
                        }
                    }

                    is ValidateNutrients.Result.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UIEvent.ShowSnackbar(result.message))
                        }
                    }
                }
            }
        }
    }
}