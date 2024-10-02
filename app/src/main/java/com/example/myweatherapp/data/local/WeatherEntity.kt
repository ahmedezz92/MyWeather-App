package com.example.myweatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myweatherapp.data.remote.model.Condition

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    val cityName: String,
    val country: String,
    val region: String,
    val temperature: Double,
    val conditionIcon: String,
    val conditionText:String,
    val windDegree: Int,
    val windSpeed: Double,
    val humidity: Int,
    val lastUpdate: String,
    val feelslike:Double,
    val timestamp: Long = System.currentTimeMillis()
)