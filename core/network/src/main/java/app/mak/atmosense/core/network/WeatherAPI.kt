package app.mak.atmosense.core.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient

interface WeatherAPI {
  suspend fun getCurrentWeather(): String
}

@ContributesBinding(scope = AppScope::class)
@Inject
internal class OpenWeatherMapAPI(
  private val httpClient: HttpClient
) : WeatherAPI {
  override suspend fun getCurrentWeather(): String {
    return "Current weather from OpenWeatherMap"
  }
}
