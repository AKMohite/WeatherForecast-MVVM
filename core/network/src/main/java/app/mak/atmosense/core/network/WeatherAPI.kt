package app.mak.atmosense.core.network

interface WeatherAPI {
  suspend fun getCurrentWeather(): String
}

internal class OpenWeatherMapAPI : WeatherAPI {
  override suspend fun getCurrentWeather(): String {
    return "Current weather from OpenWeatherMap"
  }
}
