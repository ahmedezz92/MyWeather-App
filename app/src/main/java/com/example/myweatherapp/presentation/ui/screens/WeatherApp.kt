package com.example.myweatherapp.presentation.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myweatherapp.presentation.ui.components.MovieDetailsScreen
//import com.example.myweatherapp.presentation.ui.components.MovieDetailsScreen
import com.example.myweatherapp.presentation.ui.components.SearchScreen


@Preview
@Composable
fun WeatherApp(weatherViewModel: WeatherViewModel = hiltViewModel()) {
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
                val movieId = backStackEntry.arguments?.getInt("cityName") ?: return@composable
                MovieDetailsScreen(
                    weatherViewModel = weatherViewModel,
                    movieId = movieId,
                    navController
                )
            }
        }
    }
}



