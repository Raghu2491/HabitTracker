package com.jrg.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RestaurantsViewModel : ViewModel() {

    val state = mutableStateOf(dummyRestaurants)

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        restaurants[itemIndex] =
            restaurants[itemIndex].copy(isLiked = !restaurants[itemIndex].isLiked)
        state.value = restaurants
    }

}