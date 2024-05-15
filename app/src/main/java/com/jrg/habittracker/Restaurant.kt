package com.jrg.habittracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurants")
data class Restaurant(
    var isLiked: Boolean,
    @PrimaryKey
    @ColumnInfo("r_id")
    @SerializedName("r_id")
    val id: Int,
    @ColumnInfo("r_title")
    @SerializedName("r_title")
    val title: String,
    @ColumnInfo("r_description")
    @SerializedName("r_description")
    val description: String
)