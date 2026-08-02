package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.data.mapper.toAppException
import app.mak.atmosense.core.data.mapper.toCityEntity
import app.mak.atmosense.core.data.mapper.toCityWeather
import app.mak.atmosense.core.data.mapper.toCurrentWeatherEntity
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

  override fun observeCitiesWeather(): Flow<List<CityWeather>> {
    return currentWeatherDAO.observeCitiesWeather()
      .map { weatherForCities ->
        weatherForCities.toCityWeather()
      }
  }
}

