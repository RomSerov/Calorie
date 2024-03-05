package com.example.core.domain.model

sealed class Gender(val name: String) {
    data object Male: Gender(name = MALE)
    data object Female: Gender(name = FEMALE)

    companion object {

        const val MALE = "male"
        const val FEMALE = "female"

        fun fromString(name: String): Gender {
            return when(name) {
                MALE -> Male
                FEMALE -> Female
                else -> Female
            }
        }
    }
}
