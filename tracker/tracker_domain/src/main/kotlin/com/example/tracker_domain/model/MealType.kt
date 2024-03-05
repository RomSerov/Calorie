package com.example.tracker_domain.model

sealed class MealType(val name: String) {
    data object Breakfast: MealType(name = BREAKFAST)
    data object Lunch: MealType(name = LUNCH)
    data object Dinner: MealType(name = DINNER)
    data object Snack: MealType(name = SNACK)

    companion object {
        const val BREAKFAST = "breakfast"
        const val LUNCH = "lunch"
        const val DINNER = "dinner"
        const val SNACK = "snack"

        fun fromString(name: String): MealType {
            return when(name.lowercase()) {
                BREAKFAST -> Breakfast
                LUNCH -> Lunch
                DINNER -> Dinner
                SNACK -> Snack
                else -> Breakfast
            }
        }
    }
}
