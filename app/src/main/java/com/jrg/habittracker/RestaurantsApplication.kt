package com.jrg.habittracker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RestaurantsApplication: Application(){
    init {
        app = this
    }

    companion object{
        private lateinit var app: RestaurantsApplication
        fun getAppContext(): Context = app.applicationContext
    }
}