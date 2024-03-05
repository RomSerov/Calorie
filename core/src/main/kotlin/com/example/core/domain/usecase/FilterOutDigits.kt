package com.example.core.domain.usecase

class FilterOutDigits {

    operator fun invoke(text: String): String {
        return text.filter { char ->
            char.isDigit()
        }
    }
}