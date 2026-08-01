package app.mak.atmosense.core.domain.repository

import app.mak.atmosense.core.common.model.LocationCoordinate

interface WeatherRepository {
  suspend fun fetchCurrentWeather(coordinates: LocationCoordinate)
}
