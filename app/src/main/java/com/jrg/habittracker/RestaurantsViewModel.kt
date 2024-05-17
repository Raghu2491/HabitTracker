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

class RestaurantsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private var restInterface: RestaurantsApiService
    val state = mutableStateOf(RestaurantsScreenState(
        restaurants = listOf(),
        isLoading = true
    ))
    private lateinit var restaurantsCall: Call<List<Restaurant>>
    private var restaurantsDao =
        RestaurantsDB.getDaoInstance(RestaurantsApplication.getAppContext())

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://restaurants-db-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            RestaurantsApiService::class.java
        )
        getRestaurants()
    }

    private suspend fun getAllRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                val restaurants = restInterface.getRestaurants()
                if(restaurantsDao.getAll().isEmpty())
                    restaurantsDao.addAll(restaurants)
                val favoriteRestaurants = restaurantsDao.getAllFavorites()
                restaurantsDao.updateAll(
                    favoriteRestaurants.map {
                        PartialRestaurant(it.id, isLiked = true)
                    }
                )
                return@withContext restaurantsDao.getAll()

            } catch (e: Exception) {
                return@withContext restaurantsDao.getAll()
            }
        }
    }

    fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = getAllRestaurants()
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
        saveSelection(restaurants[itemIndex])
        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(id, restaurants[itemIndex].isLiked)
            state.value = state.value.copy(restaurants = updatedRestaurants, isLoading = false)
        }
    }

    private suspend fun toggleFavoriteRestaurant(id: Int, isLiked: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(
                    id,
                    isLiked
                )
            )
            restaurantsDao.getAll()
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
            val restaurantMap = this.associateBy { it.id }.toMutableMap()
            savedIDs.forEach { id ->
                val restaurant = restaurantMap[id] ?: return@forEach
                restaurantMap[id] = restaurant.copy(isLiked = true)
            }
            return restaurantMap.values.toList()
        }
        return this
    }

    companion object {
        const val FAVORITE = "favorite"
    }

    override fun onCleared() {
        super.onCleared()
        restaurantsCall.cancel()
    }
}