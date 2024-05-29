package com.jrg.habittracker.presentation.list

import com.jrg.habittracker.domain.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean
)