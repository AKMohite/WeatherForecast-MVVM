package app.mak.atmosense.core.domain.repository

interface WeatherRepository {
  suspend fun getCurrentWeather(): String
}
