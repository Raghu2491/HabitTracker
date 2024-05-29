package com.jrg.habittracker.domain

class GetSortedRestaurantsUseCase {
    operator fun invoke(restaurants: List<Restaurant>): List<Restaurant> =
        restaurants.sortedBy { it.title }
}