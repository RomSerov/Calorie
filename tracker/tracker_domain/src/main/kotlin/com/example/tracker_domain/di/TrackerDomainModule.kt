package com.example.tracker_domain.di

import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.repositiry.TrackerRepository
import com.example.tracker_domain.usecase.CalculateMealNutrients
import com.example.tracker_domain.usecase.DeleteTrackedFood
import com.example.tracker_domain.usecase.GetFoodsForDate
import com.example.tracker_domain.usecase.SearchFood
import com.example.tracker_domain.usecase.TrackFood
import com.example.tracker_domain.usecase.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }
}
