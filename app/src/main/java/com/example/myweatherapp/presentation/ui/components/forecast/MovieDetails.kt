package com.example.myweatherapp.presentation.ui.components.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myweatherapp.presentation.ui.screens.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(weatherViewModel: WeatherViewModel, movieId: Int, navController: NavController) {
    val movie = weatherViewModel.getCityCurrentWeather("s")
    movie?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Movie Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            //  movie details content
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Movie poster
//                AsyncImage(
//                    model = Constants.URL.URL_IMAGE.plus(movie.poster_path),
//                    contentDescription = "${movie.title} poster",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp),
//                    contentScale = ContentScale.Fit
//                )

                // Movie details
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = movie.title,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Release Date: ${movie.release_date}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Overview: ${movie.overview}",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Language : ${movie.original_language}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = "Adult :${movie.adult}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
            }
        }
    }
}