package com.example.daggerhilttest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.daggerhilttest.screens.CurrentWeatherScreenV2
import com.example.daggerhilttest.screens.Destination
import com.example.daggerhilttest.screens.FetchLocationScreen
import com.example.daggerhilttest.viewmodels.WeatherViewModel

@Composable
fun WeatherNavigation(weatherViewModel: WeatherViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.LOCATION_FETCH) {
        composable(
            route = Destination.LOCATION_FETCH
        ) {
            FetchLocationScreen(onLocationFetch = { lat, lng ->
                navController.navigate(route = "${Destination.CURRENT_WEATHER}/${lat.toFloat()}/${lng.toFloat()}")
            })
        }
        composable(route = "${Destination.CURRENT_WEATHER}/{lat}/{lng}", arguments = listOf(
            navArgument(name = "lat") {
                type = NavType.FloatType
                defaultValue = 0f
            }, navArgument(name = "lng") {
                type = NavType.FloatType
                defaultValue = 0f
            }
        )) { navBackStackEntry ->
            CurrentWeatherScreenV2(
                weatherViewModel = weatherViewModel,
                currentLat = navBackStackEntry.arguments?.getFloat("lat") ?: 0f,
                currentLong = navBackStackEntry.arguments?.getFloat("lng") ?: 0f
            )
        }
    }
}