package com.jrg.habittracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jrg.habittracker.presentation.details.RestaurantDetailsScreen
import com.jrg.habittracker.presentation.details.RestaurantDetailsViewModel
import com.jrg.habittracker.presentation.list.RestaurantScreen
import com.jrg.habittracker.presentation.list.RestaurantsViewModel
import com.jrg.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RestaurantsApp()
                }
            }
        }
    }
}

@Composable
private fun RestaurantsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "restaurants") {
        composable(route = "restaurants") {
            val vm: RestaurantsViewModel = hiltViewModel()
            RestaurantScreen(vm.state.value, onFavoriteClick = { id ->
                vm.toggleFavorite(id)
            }, onItemClick = { id ->
                navController.navigate("restaurants/$id")
            })
        }
        composable(
            route = "restaurants/{restaurant_id}",
            arguments = listOf(navArgument("restaurant_id") {
                type = NavType.IntType
            })
        ) {
            val vm: RestaurantDetailsViewModel = hiltViewModel()
            RestaurantDetailsScreen(vm.state.value)
        }
    }
}
