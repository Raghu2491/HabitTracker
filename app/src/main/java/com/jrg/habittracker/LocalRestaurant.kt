package com.jrg.habittracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "restaurants")
data class LocalRestaurant(
    @ColumnInfo("is_liked")
    val isLiked: Boolean = false,
    @PrimaryKey
    @ColumnInfo("r_id")
    val id: Int,
    @ColumnInfo("r_title")
    val title: String,
    @ColumnInfo("r_description")
    val description: String
)