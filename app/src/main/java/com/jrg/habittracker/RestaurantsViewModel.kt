package com.jrg.habittracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jrg.habittracker.network.RestaurantsApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private var restInterface: RestaurantsApiService
    val state = mutableStateOf(emptyList<Restaurant>())
    private lateinit var restaurantsCall: Call<List<Restaurant>>

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
        restInterface.getRestaurants()
    }

    fun getRestaurants() {
        restaurantsCall.enqueue(
            object : Callback<List<Restaurant>> {
                override fun onResponse(
                    call: Call<List<Restaurant>>,
                    response: Response<List<Restaurant>>
                ) {
                    response.body()?.let { restaurants ->
                        state.value =
                            restaurants.restoreSelections()
                    }
                }

                override fun onFailure(
                    call: Call<List<Restaurant>>, t: Throwable
                ) {
                    t.printStackTrace()
                }
            })
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