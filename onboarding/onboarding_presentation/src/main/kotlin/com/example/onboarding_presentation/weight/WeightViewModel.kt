package com.example.onboarding_presentation.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.R
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.usecase.FilterOutDigits
import com.example.core.util.UIEvent
import com.example.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    private val _weight = MutableStateFlow("80.0")
    val weight = _weight.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    fun onWeightEnter(weight: String) {
        if (weight.length <= 5) {
            _weight.value = weight
        }
    }

    fun onNextClick() {
        job?.cancel()
        job = viewModelScope.launch {
            val weightValue = weight.value.toFloatOrNull()
            if (weightValue != null) {
                preferences.saveWeight(weightValue)
                _uiEvent.send(UIEvent.Success)
            } else {
                _uiEvent.send(
                    UIEvent.ShowSnackbar(
                        message = UiText.StringResource(
                            resId = R.string.error_weight_cant_be_empty
                        )
                    )
                )
            }
        }
    }
}