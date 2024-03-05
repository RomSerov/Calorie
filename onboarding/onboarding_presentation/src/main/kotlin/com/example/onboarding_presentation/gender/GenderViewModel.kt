package com.example.onboarding_presentation.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Gender
import com.example.core.domain.preferences.Preferences
import com.example.core.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    private val _gender = MutableStateFlow<Gender>(Gender.Male)
    val gender = _gender.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onGenderClick(gender: Gender) {
        _gender.update { gender }
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGender(gender = _gender.value)
            _uiEvent.send(UIEvent.Success)
        }
    }
}