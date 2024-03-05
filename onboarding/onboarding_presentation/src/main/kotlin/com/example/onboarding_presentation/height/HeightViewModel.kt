package com.example.onboarding_presentation.height

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
class HeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
): ViewModel() {

    private val _height = MutableStateFlow("180")
    val height = _height.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var job: Job? = null

    fun onHeightEnter(height: String) {
        if (height.length <= 3) {
            _height.value = filterOutDigits(height)
        }
    }

    fun onNextClick() {
        job?.cancel()
        job = viewModelScope.launch {
            val heightValue = height.value.toIntOrNull()
            if (heightValue != null) {
                preferences.saveHeight(heightValue)
                _uiEvent.send(UIEvent.Success)
            } else {
                _uiEvent.send(
                    UIEvent.ShowSnackbar(
                        message = UiText.StringResource(
                            resId = R.string.error_height_cant_be_empty
                        )
                    )
                )
            }
        }
    }
}