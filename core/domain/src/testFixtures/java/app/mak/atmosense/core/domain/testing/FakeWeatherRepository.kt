package app.mak.atmosense.core.domain.testing

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWeatherRepository : WeatherRepository {
  var currentWeather: CityWeather? = null
    set(value) {
      field = value
      currentWeatherFlow.value = value
    }
  private val currentWeatherFlow = MutableStateFlow<CityWeather?>(null)

  var forecastWeather: List<ForecastSlot> = emptyList()
    set(value) {
      field = value
      forecastWeatherFlow.value = value
    }
  private val forecastWeatherFlow = MutableStateFlow<List<ForecastSlot>>(emptyList())

  var fetchResult: AppResult<Unit> = AppResult.Success(Unit)
  var fetchForecastResult: AppResult<Unit> = AppResult.Success(Unit)

  // Results per city ID
  val cityFetchResults = mutableMapOf<Long, AppResult<Unit>>()
  val cityForecastResults = mutableMapOf<Long, AppResult<Unit>>()

  // Call tracking
  val fetchedCityIds = mutableListOf<Long>()
  val fetchedForecastCityIds = mutableListOf<Long>()

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
    fetchedCityIds.add(cityId)
    return cityFetchResults[cityId] ?: fetchResult
  }

  override suspend fun fetchForecastWeatherForCity(
    isForceRefresh: Boolean,
    cityId: Long
  ): AppResult<Unit> {
    error?.let { throw it }
    passedIsForceRefresh = isForceRefresh
    passedCityId = cityId
    fetchedForecastCityIds.add(cityId)
    return cityForecastResults[cityId] ?: fetchForecastResult
  }

  override fun observeCitiesWeather(): Flow<List<CityWeather>> = TODO()
  override fun observeCityForecastWeather(cityId: Long): Flow<List<ForecastSlot>> =
    forecastWeatherFlow

  override fun observeCityCurrentWeather(cityId: Long): Flow<CityWeather?> = currentWeatherFlow
}
