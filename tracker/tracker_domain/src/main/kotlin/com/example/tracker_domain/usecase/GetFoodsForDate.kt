package com.example.tracker_domain.usecase

import com.example.tracker_domain.model.TrackedFood
import com.example.tracker_domain.repositiry.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(private val repository: TrackerRepository) {

    operator fun invoke(date: LocalDate): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(localDate = date)
    }
}