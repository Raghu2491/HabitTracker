package com.jrg.habittracker.domain

import com.jrg.habittracker.data.RestaurantsRepository

class GetRestaurantsUseCase {
    private val restaurantRepository: RestaurantsRepository = RestaurantsRepository()
    suspend operator fun invoke(): List<Restaurant> {
        return GetSortedRestaurantsUseCase().invoke(restaurantRepository.loadRestaurants())
    }
}