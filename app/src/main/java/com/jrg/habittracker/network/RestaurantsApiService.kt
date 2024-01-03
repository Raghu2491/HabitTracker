package com.jrg.habittracker.network

import com.jrg.habittracker.Restaurant
import retrofit2.http.GET

interface RestaurantsApiService {
    @GET("restaurants.json")
    suspend fun getRestaurants(): List<Restaurant>
}