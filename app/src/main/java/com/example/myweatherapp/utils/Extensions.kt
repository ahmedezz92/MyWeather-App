package com.example.myweatherapp.utils

import com.example.myweatherapp.data.local.WeatherEntity
import com.example.myweatherapp.data.remote.model.Condition
import com.example.myweatherapp.data.remote.model.Current
import com.example.myweatherapp.data.remote.model.Location
import com.example.myweatherapp.data.remote.model.WeatherResponse

fun WeatherEntity.toCurrentWeather(): WeatherResponse {
    return WeatherResponse(
        location = Location(
            name = this.cityName, // The city name stored in the entity
            country = this.country,
            region = this.region
        ),
        current = Current(
            temp_c = this.temperature,
            condition = Condition(
                text = this.conditionText,
                icon = this.conditionIcon
            ),
            wind_degree = this.windDegree,
            wind_kph = this.windSpeed,
            humidity = this.humidity,
            last_updated = this.lastUpdate,
            feelslike_c = this.feelslike
        )


    )
}

fun WeatherResponse.toWeatherEntity(cityName: String): WeatherEntity {
    return WeatherEntity(
        cityName = cityName,
        country = this.location.country,
        region = this.location.region,
        temperature = this.current.temp_c,
        conditionText = this.current.condition.text,
        conditionIcon = this.current.condition.icon,
        windDegree = this.current.wind_degree,
        windSpeed = this.current.wind_kph,
        humidity = this.current.humidity,
        lastUpdate = this.current.last_updated,
        feelslike = this.current.feelslike_c
    )
}