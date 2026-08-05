package app.mak.atmosense.core.domain.repository

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.common.model.SearchCity
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
  suspend fun fetchCurrentWeather(coordinates: LocationCoordinate): AppResult<Unit>

  suspend fun searchCities(query: String): AppResult<List<SearchCity>>
  suspend fun fetchCurrentWeatherForCity(isForceRefresh: Boolean, cityId: Long): AppResult<Unit>
  suspend fun fetchForecastWeatherForCity(isForceRefresh: Boolean, cityId: Long): AppResult<Unit>
  fun observeCitiesWeather(): Flow<List<CityWeather>>
  fun observeCityCurrentWeather(cityId: Long): Flow<CityWeather?>
  fun observeCityForecastWeather(cityId: Long): Flow<List<ForecastSlot>>
}
