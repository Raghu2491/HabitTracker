package com.jrg.habittracker

class ToggleRestaurantsUseCase {
    private val restaurantRepository: RestaurantsRepository = RestaurantsRepository()
    suspend operator fun invoke(id: Int, isLiked: Boolean): List<Restaurant> {
        restaurantRepository.toggleFavoriteRestaurant(id, isLiked.not())
        return GetSortedRestaurantsUseCase().invoke(restaurantRepository.getCachedRestaurants())
    }
}