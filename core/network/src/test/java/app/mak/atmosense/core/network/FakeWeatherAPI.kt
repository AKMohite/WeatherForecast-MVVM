package app.mak.atmosense.core.network

import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.dto.ForecastWeatherDTO
import app.mak.atmosense.core.network.dto.LocationDTO

class FakeWeatherAPI : WeatherAPI {
  var currentWeatherResult: CurrentWeatherDTO? = null
  var searchLocationsResult: List<LocationDTO> = emptyList()
  var forecastWeatherResult: ForecastWeatherDTO? = null

  var error: Throwable? = null

  override suspend fun getCurrentWeather(queries: Map<String, String>): CurrentWeatherDTO {
    error?.let { throw it }
    return currentWeatherResult ?: throw IllegalStateException("CurrentWeatherDTO not set")
  }

  override suspend fun searchLocations(queries: Map<String, String>): List<LocationDTO> {
    error?.let { throw it }
    return searchLocationsResult
  }

  override suspend fun getForecastWeather(queries: Map<String, String>): ForecastWeatherDTO {
    error?.let { throw it }
    return forecastWeatherResult ?: throw IllegalStateException("ForecastWeatherDTO not set")
  }
}
