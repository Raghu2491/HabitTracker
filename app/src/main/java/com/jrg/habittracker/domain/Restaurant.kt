package com.jrg.habittracker.domain

data class Restaurant(
    val isLiked: Boolean = false,
    val id: Int,
    val title: String,
    val description: String
)