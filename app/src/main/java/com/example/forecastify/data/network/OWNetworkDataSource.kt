package com.example.forecastify.data.network

import com.example.forecastify.data.network.response.CurrentWeatherDTO
import com.example.forecastify.data.network.response.WeatherForecastDTO

class OWNetworkDataSource(
    private val api: OpenWeatherAPI
): IOWNetworkDataSource {
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unitSystem: String
    ): CurrentWeatherDTO {
        return api.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            language = language,
            unitSystem = unitSystem
        )
    }

    override suspend fun getFutureWeather(
        latitude: Double,
        longitude: Double,
        dayCount: Int,
        language: String,
        unitSystem: String
    ): WeatherForecastDTO {
        return api.getFutureWeather(
            latitude = latitude,
            longitude = longitude,
            dayCount = dayCount,
            language = language,
            unitSystem = unitSystem
        )
    }
}

interface IOWNetworkDataSource {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String,
        unitSystem: String
    ): CurrentWeatherDTO

    suspend fun getFutureWeather(
        latitude: Double,
        longitude: Double,
        dayCount: Int,
        language: String,
        unitSystem: String
    ): WeatherForecastDTO
}