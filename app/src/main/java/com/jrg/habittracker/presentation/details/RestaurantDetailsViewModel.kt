package com.jrg.habittracker.presentation.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrg.habittracker.domain.Restaurant
import com.jrg.habittracker.data.remote.RestaurantsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val restInterface: RestaurantsApiService
) : ViewModel() {

    val state = mutableStateOf<Restaurant?>(null)

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0

        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(id)
            state.value = restaurant
        }
    }

    private suspend fun getRemoteRestaurant(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface
                .getRestaurant(id)
            return@withContext responseMap.values.first().let {
                Restaurant(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }
        }
    }
}