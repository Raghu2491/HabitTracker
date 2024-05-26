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

    val restaurantsUseCase = GetRestaurantsUseCase()
    val toggleRestaurantsUseCase = ToggleRestaurantsUseCase()

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = restaurantsUseCase()
            _state.value = _state.value.copy(
                restaurants = restaurants,
                isLoading = false
            )
        }
    }

    fun toggleFavorite(id: Int) {
        val restaurants = _state.value.restaurants.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        viewModelScope.launch {
            val updatedRestaurants =
                toggleRestaurantsUseCase(id, restaurants[itemIndex].isLiked)
            _state.value = _state.value.copy(restaurants = updatedRestaurants, isLoading = false)
        }
    }
}