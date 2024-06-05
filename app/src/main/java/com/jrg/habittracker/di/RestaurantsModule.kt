package com.jrg.habittracker.di

import android.content.Context
import androidx.room.Room
import com.jrg.habittracker.data.local.RestaurantsDB
import com.jrg.habittracker.data.local.RestaurantsDao
import com.jrg.habittracker.data.remote.RestaurantsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RestaurantsModule {

    @Singleton
    @Provides
    fun provideRestClient(): RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-db-default-rtdb.firebaseio.com/")
            .build()
            .create(RestaurantsApiService::class.java)


    @Provides
    fun providesRestaurantDao(database: RestaurantsDB): RestaurantsDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun provideRoomDataBase(
        @ApplicationContext context: Context
    ): RestaurantsDB {
        return Room.databaseBuilder(
            context.applicationContext,
            RestaurantsDB::class.java,
            "restaurants_database"
        ).fallbackToDestructiveMigration().build()
    }

}