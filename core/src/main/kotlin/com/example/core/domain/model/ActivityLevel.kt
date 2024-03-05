package com.example.core.domain.model

sealed class ActivityLevel(val name: String) {
    data object Low : ActivityLevel(name = LOW)
    data object Medium : ActivityLevel(name = MEDIUM)
    data object High : ActivityLevel(name = HIGH)

    companion object {

        const val LOW = "low"
        const val MEDIUM = "medium"
        const val HIGH = "high"

        fun fromString(name: String): ActivityLevel {
            return when (name) {
                LOW -> Low
                MEDIUM -> Medium
                HIGH -> High
                else -> Medium
            }
        }
    }
}
