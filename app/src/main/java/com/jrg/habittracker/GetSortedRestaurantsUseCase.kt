package com.jrg.habittracker

class GetSortedRestaurantsUseCase {
    operator fun invoke(restaurants: List<Restaurant>): List<Restaurant> =
        restaurants.sortedBy { it.title }
}