package com.jrg.habittracker.presentation.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.jrg.habittracker.domain.GetRestaurantsUseCase
import com.jrg.habittracker.domain.ToggleRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val restaurantsUseCase: GetRestaurantsUseCase,
    private val toggleRestaurantsUseCase: ToggleRestaurantsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true
        )
    )

    val state: State<RestaurantsScreenState>
        get() = _state

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