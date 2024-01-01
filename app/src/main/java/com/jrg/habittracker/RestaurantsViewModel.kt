package com.jrg.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RestaurantsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val state = mutableStateOf(dummyRestaurants.restoreSelections())

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        restaurants[itemIndex] =
            restaurants[itemIndex].copy(isLiked = !restaurants[itemIndex].isLiked)
        saveSelection(restaurants[itemIndex])
        state.value = restaurants
    }

    private fun saveSelection(item: Restaurant) {
        val savedInstance = savedStateHandle
            .get<List<Int>?>(FAVORITE)
            .orEmpty()
            .toMutableList()
        if (item.isLiked)
            savedInstance.add(item.id)
        else
            savedInstance.remove(item.id)
        savedStateHandle[FAVORITE] = savedInstance
    }

    private fun List<Restaurant>.restoreSelections(): List<Restaurant> {
        savedStateHandle.get<List<Int>?>(FAVORITE)?.let { savedIDs ->
            val restaurantMap = this.associateBy { it.id }
            savedIDs.forEach { id ->
                restaurantMap[id]?.isLiked = true
            }
            return restaurantMap.values.toList()
        }
        return this
    }

    companion object {
        const val FAVORITE = "favorite"
    }
}