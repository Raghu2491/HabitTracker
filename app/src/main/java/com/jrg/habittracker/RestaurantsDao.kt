package com.jrg.habittracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RestaurantsDao {

    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)

    @Update(entity = Restaurant::class)
    suspend fun update(partialRestaurant: PartialRestaurant)

    @Query("SELECT * FROM restaurants WHERE is_liked=1")
    suspend fun getAllFavorites(): List<Restaurant>

    @Update(entity = Restaurant::class)
    suspend fun updateAll(partialRestaurant: List<PartialRestaurant>)
}