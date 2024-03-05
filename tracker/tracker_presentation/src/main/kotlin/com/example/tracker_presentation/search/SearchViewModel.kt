package com.example.tracker_presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.FilterOutDigits
import com.example.core.util.UIEvent
import com.example.core.util.UiText
import com.example.tracker_domain.usecase.TrackerUseCases
import com.example.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.OnQueryChange -> {
                _state.update {
                    it.copy(query = event.query)
                }
            }
            is SearchEvent.OnAmountForFoodChange -> {
                _state.update {
                    it.copy(
                        trackableFood = state.value.trackableFood.map { uiState ->
                            if(uiState.food == event.food) {
                                uiState.copy(amount = filterOutDigits(event.amount))
                            } else uiState
                        }
                    )
                }
            }
            is SearchEvent.OnSearch -> {
                executeSearch()
            }
            is SearchEvent.OnToggleTrackableFood -> {
                _state.update { searchState ->
                    searchState.copy(
                        trackableFood = state.value.trackableFood.map { uiState ->
                            if(uiState.food == event.food) {
                                uiState.copy(isExpanded = !uiState.isExpanded)
                            } else uiState
                        }
                    )
                }
            }
            is SearchEvent.OnSearchFocusChange -> {
                _state.update {
                    it.copy(
                        isHintVisible = !event.isFocused && state.value.query.isBlank()
                    )
                }
            }
            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    isSearching = true,
                    trackableFood = emptyList()
                )
            }

            trackerUseCases
                .searchFood(query = state.value.query)
                .onSuccess { foods ->
                    _state.update {
                        it.copy(
                            trackableFood = foods.map { trackableFood ->
                                TrackableFoodUiState(food = trackableFood)
                            },
                            isSearching = false,
                            query = ""
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(isSearching = false)
                    }

                    _uiEvent.send(
                        UIEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.value.trackableFood.find { it.food == event.food }
            trackerUseCases.trackFood(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UIEvent.NavigateUp)
        }
    }
}