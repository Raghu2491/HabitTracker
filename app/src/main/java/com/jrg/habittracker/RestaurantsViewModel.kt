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
    val state = mutableStateOf(emptyList<Restaurant>())
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

    private suspend fun getRemoteRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                val restaurants = restInterface.getRestaurants()
                restaurantsDao.addAll(restaurants)
                return@withContext restaurants

            } catch (e: Exception) {
                return@withContext restaurantsDao.getAll()
            }
        }
    }

    fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = getRemoteRestaurants()
            state.value = restaurants.restoreSelections()
        }
    }

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

    override fun onCleared() {
        super.onCleared()
        restaurantsCall.cancel()
    }
}