package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.network.WeatherAPI

internal class DefaultWeatherRepository(
  private val weatherAPI: WeatherAPI
) : WeatherRepository {
  override suspend fun getCurrentWeather(): String {
    return weatherAPI.getCurrentWeather()
  }
}
