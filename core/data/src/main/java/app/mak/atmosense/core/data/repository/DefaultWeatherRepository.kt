package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.common.model.SyncRequestType
import app.mak.atmosense.core.data.mapper.toAppException
import app.mak.atmosense.core.data.mapper.toCityEntity
import app.mak.atmosense.core.data.mapper.toCurrentWeatherEntity
import app.mak.atmosense.core.data.mapper.toForecast
import app.mak.atmosense.core.data.mapper.toForecastEntity
import app.mak.atmosense.core.data.mapper.toObserveCity
import app.mak.atmosense.core.data.mapper.toSearchCities
import app.mak.atmosense.core.data.mapper.toWeatherCities
import app.mak.atmosense.core.database.dao.SyncEntity
import app.mak.atmosense.core.database.dao.api.CityDAO
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import app.mak.atmosense.core.database.dao.api.DatabaseTransaction
import app.mak.atmosense.core.database.dao.api.ForecastWeatherDAO
import app.mak.atmosense.core.database.dao.api.SyncDAO
import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.network.WeatherAPI
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

@ContributesBinding(scope = AppScope::class)
@Inject
class DefaultWeatherRepository(
  private val weatherAPI: WeatherAPI,
  private val cityDAO: CityDAO,
  private val currentWeatherDAO: CurrentWeatherDAO,
  private val syncDAO: SyncDAO,
  private val forecastWeatherDAO: ForecastWeatherDAO,
  private val dbTransaction: DatabaseTransaction,
  private val clock: Clock
) : WeatherRepository {

  override suspend fun fetchCurrentWeather(coordinates: LocationCoordinate): AppResult<Unit> {
    return try {
      val queries = mapOf(
        "lat" to coordinates.latitude.toString(),
        "lon" to coordinates.longitude.toString(),
        "unit" to "metric"
      )
      val currentWeather = weatherAPI.getCurrentWeather(queries)
      val now = clock.now()
      val city = currentWeather.toCityEntity(now)
      dbTransaction {
        cityDAO.insert(city)
        currentWeatherDAO.insert(currentWeather.toCurrentWeatherEntity(now))
        syncDAO.insert(
          SyncEntity(
            city_id = city.id, sync_type = SyncRequestType.CurrentWeather.name,
            last_attempt_at = now, last_success_at = now
          )
        )
      }
      AppResult.Success(Unit)
    } catch (t: Throwable) {
      AppResult.Failure(exception = t.toAppException())
    }
  }

  override suspend fun searchCities(query: String): AppResult<List<SearchCity>> {
    return try {
      val queries = mapOf(
        "q" to query,
        "limit" to "10",
      )
      val locations = weatherAPI.searchLocations(queries)
      val searchResults = locations.toSearchCities()
      AppResult.Success(searchResults)
    } catch (t: Throwable) {
      AppResult.Failure(exception = t.toAppException())
    }
  }

  override suspend fun fetchCurrentWeatherForCity(
    isForceRefresh: Boolean,
    cityId: Long
  ): AppResult<Unit> {
    val city = cityDAO.getById(cityId) ?: throw AppException(
      AppError.EntityNotFound,
      Throwable("City not found")
    )
    if (!isForceRefresh) {
      val syncEntity = syncDAO.getSyncStatus(cityId, SyncRequestType.CurrentWeather.name)
      val lastSyncedAt = syncEntity?.last_success_at
      if (lastSyncedAt != null && isRequestValid(lastSyncedAt, 1.hours)) {
        return AppResult.Success(Unit)
      }
    }
    val coordinates = LocationCoordinate(city.latitude, city.longitude)
    return fetchCurrentWeather(coordinates)
  }

  override suspend fun fetchForecastWeatherForCity(
    isForceRefresh: Boolean,
    cityId: Long
  ): AppResult<Unit> {
    val city = cityDAO.getById(cityId) ?: throw AppException(
      AppError.EntityNotFound,
      Throwable("City not found")
    )
    if (!isForceRefresh) {
      val syncEntity = syncDAO.getSyncStatus(cityId, SyncRequestType.ForecastWeather.name)
      val lastSyncedAt = syncEntity?.last_success_at
      if (lastSyncedAt != null && isRequestValid(lastSyncedAt, 1.hours)) {
        return AppResult.Success(Unit)
      }
    }
    val coordinates = LocationCoordinate(city.latitude, city.longitude)
    return fetchForecastWeather(cityId, coordinates)
  }

  private suspend fun fetchForecastWeather(
    cityId: Long,
    coordinates: LocationCoordinate
  ): AppResult<Unit> {
    return try {
      val queries = mapOf(
        "lat" to coordinates.latitude.toString(),
        "lon" to coordinates.longitude.toString(),
        "unit" to "metric"
      )
      val forecastWeather = weatherAPI.getForecastWeather(queries)
      val forecastSlots = forecastWeather.list?.toForecast(cityId).orEmpty()
      val now = clock.now()
      val forecastEntities = forecastSlots.map { it.toForecastEntity(now) }
      dbTransaction {
        forecastWeatherDAO.deleteByCityId(cityId)
        forecastWeatherDAO.insertAll(forecastEntities)
        syncDAO.insert(
          SyncEntity(
            city_id = cityId, sync_type = SyncRequestType.ForecastWeather.name,
            last_attempt_at = now, last_success_at = now
          )
        )
      }
      AppResult.Success(Unit)
    } catch (t: Throwable) {
      AppResult.Failure(exception = t.toAppException())
    }
  }

  override fun observeCitiesWeather(): Flow<List<CityWeather>> {
    return currentWeatherDAO.observeCitiesWeather()
      .map { weatherForCities ->
        weatherForCities.toWeatherCities()
      }
  }

  override fun observeCityForecastWeather(cityId: Long): Flow<List<ForecastSlot>> {
    return forecastWeatherDAO.observeForecastByCityId(cityId)
      .map { it.toForecast() }
  }

  override fun observeCityCurrentWeather(cityId: Long): Flow<CityWeather?> {
    return currentWeatherDAO.observeCityCurrentWeather(cityId)
      .map { it?.toObserveCity() }
  }

  private fun isRequestValid(
    lastSyncedAt: Instant,
    duration: Duration,
  ): Boolean = lastSyncedAt > (clock.now() - duration)
}


