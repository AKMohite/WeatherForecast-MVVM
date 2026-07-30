package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.network.WeatherAPI
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(scope = AppScope::class)
@Inject
internal class DefaultWeatherRepository(
  private val weatherAPI: WeatherAPI
) : WeatherRepository {
  override suspend fun getCurrentWeather(): String {
    return weatherAPI.getCurrentWeather()
  }
}
