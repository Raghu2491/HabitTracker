package com.jrg.habittracker.data

import com.jrg.habittracker.domain.Restaurant
import com.jrg.habittracker.data.local.LocalRestaurant
import com.jrg.habittracker.data.local.PartialLocalRestaurant
import com.jrg.habittracker.data.local.RestaurantsDao
import com.jrg.habittracker.data.remote.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantsRepository @Inject constructor(
    private val restInterface: RestaurantsApiService,
    private val restaurantsDao: RestaurantsDao
) {
    suspend fun loadRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteRestaurant = restInterface.getRestaurants()
                if (restaurantsDao.getAll().isEmpty())
                    restaurantsDao.addAll(remoteRestaurant.map {
                        LocalRestaurant(
                            false,
                            it.id,
                            it.title,
                            it.description
                        )
                    })
                val favoriteRestaurants = restaurantsDao.getAllFavorites()
                restaurantsDao.updateAll(
                    favoriteRestaurants.map {
                        PartialLocalRestaurant(it.id, isLiked = true)
                    }
                )
                return@withContext restaurantsDao.getAll().map {
                    createRestaurant(it)
                }

            } catch (e: Exception) {
                return@withContext restaurantsDao.getAll().map {
                    createRestaurant(it)
                }
            }
        }
    }

    suspend fun getCachedRestaurants() = restaurantsDao.getAll().map {
        createRestaurant(it)
    }

    suspend fun toggleFavoriteRestaurant(id: Int, isLiked: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialLocalRestaurant(
                    id,
                    isLiked
                )
            )
        }

    private fun createRestaurant(localRestaurant: LocalRestaurant) =
        Restaurant(
            localRestaurant.isLiked,
            localRestaurant.id,
            localRestaurant.title,
            localRestaurant.description
        )
}