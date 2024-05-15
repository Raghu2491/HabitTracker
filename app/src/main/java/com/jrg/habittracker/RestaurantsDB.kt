package com.jrg.habittracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Restaurant::class],
    version = 2,
    exportSchema = false
)
abstract class RestaurantsDB : RoomDatabase() {
    abstract val dao: RestaurantsDao

    companion object {

        @Volatile
        private var INSTANCE: RestaurantsDao? = null

        fun getDaoInstance(context: Context): RestaurantsDao{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = buildDatabase(context).dao
                    INSTANCE =instance
                }
                return instance
            }
        }

        private fun buildDatabase(context: Context): RestaurantsDB =
            Room.databaseBuilder(
                context.applicationContext,
                RestaurantsDB::class.java,
                "restaurants_database"
            ).fallbackToDestructiveMigration().build()
    }

}