package com.example.myweatherapp.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myweatherapp.presentation.ui.components.forecast.CityForecastView
//import com.example.myweatherapp.presentation.ui.components.MovieDetailsScreen
import com.example.myweatherapp.presentation.ui.components.SearchScreen
import com.example.myweatherapp.presentation.ui.components.forecast.CityForecastViewModel


@Composable
fun WeatherApp(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    cityForecastViewModel: CityForecastViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "searchScreen",
            modifier = Modifier.padding(padding)
        ) {
            composable("searchScreen") {
                SearchScreen(
                    weatherViewModel = weatherViewModel,
                    navController = navController
                )
            }
            composable(
                route = "cityWeatherForecast/{cityName}",
                arguments = listOf(navArgument("cityName") { type = NavType.StringType })
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString("cityName") ?: return@composable
                CityForecastView(
                    cityForecastViewModel = cityForecastViewModel,
                    cityName = cityName,
                    navController
                )
            }
        }
    }
}



