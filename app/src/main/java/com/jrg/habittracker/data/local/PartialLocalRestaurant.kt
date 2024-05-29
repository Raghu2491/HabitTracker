package com.jrg.habittracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PartialLocalRestaurant(
    @ColumnInfo("r_id")
    val id: Int,
    @ColumnInfo("is_liked")
    val isLiked: Boolean
)
