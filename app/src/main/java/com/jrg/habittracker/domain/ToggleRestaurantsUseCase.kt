package com.jrg.habittracker.domain

import com.jrg.habittracker.data.RestaurantsRepository
import javax.inject.Inject

class ToggleRestaurantsUseCase @Inject constructor(private val restaurantRepository: RestaurantsRepository) {
    suspend operator fun invoke(id: Int, isLiked: Boolean): List<Restaurant> {
        restaurantRepository.toggleFavoriteRestaurant(id, isLiked.not())
        return GetSortedRestaurantsUseCase().invoke(restaurantRepository.getCachedRestaurants())
    }
}