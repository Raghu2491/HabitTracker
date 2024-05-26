package com.jrg.habittracker

class GetRestaurantsUseCase {
    private val restaurantRepository: RestaurantsRepository = RestaurantsRepository()
    suspend operator fun invoke(): List<Restaurant> =
        restaurantRepository.getAllRestaurants().sortedBy { it.title }

}