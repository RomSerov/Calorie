package com.example.onboarding_presentation.age

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.R
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.usecase.FilterOutDigits
import com.example.core.util.UIEvent
import com.example.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {

    private val _age = MutableStateFlow<String>("20")
    val age = _age.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var job: Job? = null

    fun onAgeEnter(age: String) {
        if (age.length <= 3) {
            _age.value = filterOutDigits(age)
        }
    }

    fun onNextClick() {
        job?.cancel()
        job = viewModelScope.launch {
            val ageValue = age.value.toIntOrNull()
            if (ageValue != null) {
                preferences.saveAge(ageValue)
                _uiEvent.emit(UIEvent.Success)
            } else {
                _uiEvent.emit(
                    UIEvent.ShowSnackbar(
                        message = UiText.StringResource(
                            resId = R.string.error_age_cant_be_empty
                        )
                    )
                )
            }
        }
    }
}