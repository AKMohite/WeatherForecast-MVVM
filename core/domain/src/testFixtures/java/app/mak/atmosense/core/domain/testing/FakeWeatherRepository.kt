package app.mak.atmosense.core.domain.testing

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class FakeWeatherRepository : WeatherRepository {
  var fetchResult: AppResult<Unit> = AppResult.Success(Unit)
  var fetchForecastResult: AppResult<Unit> = AppResult.Success(Unit)
  var passedCoordinates: LocationCoordinate? = null
  var passedCityId: Long? = null
  var passedIsForceRefresh: Boolean? = null

  var error: Throwable? = null

  override suspend fun fetchCurrentWeather(coordinates: LocationCoordinate): AppResult<Unit> {
    error?.let { throw it }
    passedCoordinates = coordinates
    return fetchResult
  }

  override suspend fun searchCities(query: String): AppResult<List<SearchCity>> = TODO()
  override suspend fun fetchCurrentWeatherForCity(
    isForceRefresh: Boolean,
    cityId: Long
  ): AppResult<Unit> {
    error?.let { throw it }
    passedIsForceRefresh = isForceRefresh
    passedCityId = cityId
    return fetchResult
  }

  override suspend fun fetchForecastWeatherForCity(
    isForceRefresh: Boolean,
    cityId: Long
  ): AppResult<Unit> {
    error?.let { throw it }
    passedIsForceRefresh = isForceRefresh
    passedCityId = cityId
    return fetchForecastResult
  }

  override fun observeCitiesWeather(): Flow<List<CityWeather>> = TODO()
  override fun observeCityForecastWeather(cityId: Long): Flow<List<ForecastSlot>> = TODO()
  override fun observeCityCurrentWeather(cityId: Long): Flow<CityWeather?> = TODO()
}
