package com.example.core.util

import android.provider.Contacts.Intents.UI

sealed class UIEvent {

    object Empty: UIEvent()
    data object Success: UIEvent()
    data object NavigateUp: UIEvent()
    data class ShowSnackbar(val message: UiText): UIEvent()
}
