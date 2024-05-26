package com.jrg.habittracker

import com.jrg.habittracker.network.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RestaurantsRepository {
    private val restInterface: RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-db-default-rtdb.firebaseio.com/")
            .build()
            .create(RestaurantsApiService::class.java)

    private val restaurantsDao =
        RestaurantsDB.getDaoInstance(RestaurantsApplication.getAppContext())

    suspend fun loadRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                val restaurants = restInterface.getRestaurants()
                if(restaurantsDao.getAll().isEmpty())
                    restaurantsDao.addAll(restaurants)
                val favoriteRestaurants = restaurantsDao.getAllFavorites()
                restaurantsDao.updateAll(
                    favoriteRestaurants.map {
                        PartialRestaurant(it.id, isLiked = true)
                    }
                )
                return@withContext restaurantsDao.getAll()

            } catch (e: Exception) {
                return@withContext restaurantsDao.getAll()
            }
        }
    }

    suspend fun getCachedRestaurants() = restaurantsDao.getAll()

    suspend fun toggleFavoriteRestaurant(id: Int, isLiked: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(
                    id,
                    isLiked
                )
            )
        }
}