package com.jrg.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
class RestaurantsViewModel : ViewModel() {

    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true
        )
    )

    val state : State<RestaurantsScreenState>
        get() = _state

    val repository = RestaurantsRepository()

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = repository.getAllRestaurants()
            _state.value = _state.value.copy(
                restaurants = restaurants,
                isLoading = false
            )
        }
    }

    fun toggleFavorite(id: Int) {
        val restaurants = _state.value.restaurants.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        restaurants[itemIndex] =
            restaurants[itemIndex].copy(isLiked = !restaurants[itemIndex].isLiked)
        viewModelScope.launch {
            val updatedRestaurants =
                repository.toggleFavoriteRestaurant(id, restaurants[itemIndex].isLiked)
            _state.value = _state.value.copy(restaurants = updatedRestaurants, isLoading = false)
        }
    }
}