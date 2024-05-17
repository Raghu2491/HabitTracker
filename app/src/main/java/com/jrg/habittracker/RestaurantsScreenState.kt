package com.jrg.habittracker

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean
)