package com.example.myweatherapp.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myweatherapp.presentation.ui.screens.WeatherViewModel

@Composable
fun SearchScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavHostController,
) {
    var query by remember { mutableStateOf("") }
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val isError by weatherViewModel.errorCode.collectAsState()

    isLoading.takeIf { it }?.let {
        LoadingState()
    }
    isError.takeIf { it != 200 }?.let {
        ErrorState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchTextField(query = query,
            onSearch = {userInput ->
                query = userInput
                weatherViewModel.onQueryChanged(query)
            }
        )
    }

    if (query.isNotEmpty()) {
        LaunchedEffect(Unit) {
                weatherViewModel.getCityCurrentWeather(query).collect { state ->
                    weatherViewModel.handleStateCityCurrentWeather(state)
            }
        }

    }
}


