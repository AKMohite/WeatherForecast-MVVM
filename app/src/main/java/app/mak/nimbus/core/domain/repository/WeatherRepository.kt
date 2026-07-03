package app.mak.nimbus.core.domain.repository

import app.mak.nimbus.core.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String, lat: Double, lon: Double): WeatherForecast
}