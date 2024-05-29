package com.jrg.habittracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jrg.habittracker.data.local.LocalRestaurant
import com.jrg.habittracker.data.local.PartialLocalRestaurant

@Dao
interface RestaurantsDao {

    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<LocalRestaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<LocalRestaurant>)

    @Update(entity = LocalRestaurant::class)
    suspend fun update(partialRestaurant: PartialLocalRestaurant)

    @Query("SELECT * FROM restaurants WHERE is_liked=1")
    suspend fun getAllFavorites(): List<LocalRestaurant>

    @Update(entity = LocalRestaurant::class)
    suspend fun updateAll(partialRestaurant: List<PartialLocalRestaurant>)
}