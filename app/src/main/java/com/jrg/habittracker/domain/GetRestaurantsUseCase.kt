package com.jrg.habittracker.domain

import com.jrg.habittracker.data.RestaurantsRepository
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(private val restaurantRepository: RestaurantsRepository) {
    suspend operator fun invoke(): List<Restaurant> {
        return GetSortedRestaurantsUseCase().invoke(restaurantRepository.loadRestaurants())
    }
}