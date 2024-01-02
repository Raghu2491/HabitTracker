package com.jrg.habittracker.network

import com.jrg.habittracker.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantsApiService {
    @GET("restaurants.json")
    fun getRestaurants(): Call<List<Restaurant>>
}