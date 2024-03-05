package com.example.core.domain.model

sealed class GoalType(val name: String) {
    data object LoseWeight : GoalType(name = LOSE_WEIGHT)
    data object KeepWeight : GoalType(name = KEEP_WEIGHT)
    data object GainWeight : GoalType(name = GAIN_WEIGHT)

    companion object {

        const val LOSE_WEIGHT = "lose_weight"
        const val KEEP_WEIGHT = "keep_weight"
        const val GAIN_WEIGHT = "gain_weight"

        fun fromString(name: String): GoalType {
            return when (name) {
                LOSE_WEIGHT -> LoseWeight
                KEEP_WEIGHT -> KeepWeight
                GAIN_WEIGHT -> GainWeight
                else -> KeepWeight
            }
        }
    }
}
