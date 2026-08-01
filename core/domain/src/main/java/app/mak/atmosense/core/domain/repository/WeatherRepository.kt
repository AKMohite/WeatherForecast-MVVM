package app.mak.atmosense.core.domain.repository

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
  suspend fun fetchCurrentWeather(coordinates: LocationCoordinate): AppResult<Unit>
  fun observeCitiesWeather(): Flow<List<String>>
}
