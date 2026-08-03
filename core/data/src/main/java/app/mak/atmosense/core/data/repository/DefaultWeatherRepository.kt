package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.data.mapper.toAppException
import app.mak.atmosense.core.data.mapper.toCityEntity
import app.mak.atmosense.core.data.mapper.toCityWeather
import app.mak.atmosense.core.data.mapper.toCurrentWeatherEntity
import app.mak.atmosense.core.data.mapper.toForecast
import app.mak.atmosense.core.data.mapper.toSearchCities
import app.mak.atmosense.core.database.dao.api.CityDAO
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import app.mak.atmosense.core.database.dao.api.DatabaseTransaction
import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.network.WeatherAPI
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

@ContributesBinding(scope = AppScope::class)
@Inject
class DefaultWeatherRepository(
  private val weatherAPI: WeatherAPI,
  private val cityDAO: CityDAO,
  private val currentWeatherDAO: CurrentWeatherDAO,
  private val dbTransaction: DatabaseTransaction
) : WeatherRepository {

  override suspend fun fetchCurrentWeather(coordinates: LocationCoordinate): AppResult<Unit> {
    return try {
      val queries = mapOf(
        "lat" to coordinates.latitude.toString(),
        "lon" to coordinates.longitude.toString(),
        "unit" to "metric"
      )
      val currentWeather = weatherAPI.getCurrentWeather(queries)
      val now = Clock.System.now()
      dbTransaction {
        cityDAO.insert(currentWeather.toCityEntity(now))
        currentWeatherDAO.insert(currentWeather.toCurrentWeatherEntity(now))
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

  override suspend fun fetchCurrentWeatherForCity(cityId: Long): AppResult<Unit> {
    val city = cityDAO.getById(cityId) ?: throw AppException(
      AppError.EntityNotFound,
      Throwable("City not found")
    )
    val coordinates = LocationCoordinate(city.latitude, city.longitude)
    return fetchCurrentWeather(coordinates)
  }

  override suspend fun fetchForecastWeatherForCity(cityId: Long): AppResult<Unit> {
    val city = cityDAO.getById(cityId) ?: throw AppException(
      AppError.EntityNotFound,
      Throwable("City not found")
    )
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
      val now = Clock.System.now()
//      val forecastEntities = forecastSlots.map { it.toEntity(now) }
      dbTransaction {
//        db.forecastDAO().deleteForCity(cityId)
//        db.forecastDAO().insertAll(forecastEntities)
//        db.syncDAO().insert(
//          SyncEntity(
//            cityId = cityId, syncType = SyncRequestType.ForecastWeather.name,
//            lastAttemptAt = now, lastSuccessAt = now
//          )
//        )
      }
      AppResult.Success(Unit)
    } catch (t: Throwable) {
      AppResult.Failure(exception = t.toAppException())
    }
  }

  override fun observeCitiesWeather(): Flow<List<CityWeather>> {
    return currentWeatherDAO.observeCitiesWeather()
      .map { weatherForCities ->
        weatherForCities.toCityWeather()
      }
  }
}

