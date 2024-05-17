package com.jrg.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrg.habittracker.network.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RestaurantsViewModel : ViewModel() {
    val state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true
        )
    )

    val repository = RestaurantsRepository()

    init {
        getRestaurants()
    }

    fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = repository.getAllRestaurants()
            state.value = state.value.copy(
                restaurants = restaurants,
                isLoading = false
            )
        }
    }

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.restaurants.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        restaurants[itemIndex] =
            restaurants[itemIndex].copy(isLiked = !restaurants[itemIndex].isLiked)
        viewModelScope.launch {
            val updatedRestaurants =
                repository.toggleFavoriteRestaurant(id, restaurants[itemIndex].isLiked)
            state.value = state.value.copy(restaurants = updatedRestaurants, isLoading = false)
        }
    }
}