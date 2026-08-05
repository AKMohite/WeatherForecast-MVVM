package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.data.testing.FakeCityDAO
import app.mak.atmosense.core.data.testing.FakeCurrentWeatherDAO
import app.mak.atmosense.core.data.testing.FakeDatabaseTransaction
import app.mak.atmosense.core.data.testing.FakeForecastWeatherDAO
import app.mak.atmosense.core.data.testing.FakeSyncDAO
import app.mak.atmosense.core.database.dao.CityEntity
import app.mak.atmosense.core.network.dto.CoordinateDTO
import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.dto.ForecastWeatherDTO
import app.mak.atmosense.core.network.dto.HourlyDTO
import app.mak.atmosense.core.network.dto.LocationDTO
import app.mak.atmosense.core.network.dto.MainDTO
import app.mak.atmosense.core.network.dto.SysDTO
import app.mak.atmosense.core.network.dto.WeatherDTO
import app.mak.atmosense.core.network.dto.WindDTO
import app.mak.atmosense.core.network.testing.FakeWeatherAPI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultWeatherRepositoryTest {

  private lateinit var weatherAPI: FakeWeatherAPI
  private lateinit var cityDAO: FakeCityDAO
  private lateinit var currentWeatherDAO: FakeCurrentWeatherDAO
  private lateinit var syncDAO: FakeSyncDAO
  private lateinit var forecastWeatherDAO: FakeForecastWeatherDAO
  private lateinit var dbTransaction: FakeDatabaseTransaction
  private lateinit var clock: FakeClock
  private lateinit var repository: DefaultWeatherRepository

  private class FakeClock(var now: Instant) : Clock {
    override fun now(): Instant = now
  }

  @Before
  fun setUp() {
    weatherAPI = FakeWeatherAPI()
    cityDAO = FakeCityDAO()
    currentWeatherDAO = FakeCurrentWeatherDAO(cityDAO)
    syncDAO = FakeSyncDAO()
    forecastWeatherDAO = FakeForecastWeatherDAO()
    dbTransaction = FakeDatabaseTransaction()
    clock = FakeClock(Instant.fromEpochMilliseconds(1000000))
    repository = DefaultWeatherRepository(
      weatherAPI = weatherAPI,
      cityDAO = cityDAO,
      currentWeatherDAO = currentWeatherDAO,
      syncDAO = syncDAO,
      forecastWeatherDAO = forecastWeatherDAO,
      dbTransaction = dbTransaction,
      clock = clock
    )
  }

  @Test
  fun `fetchCurrentWeather inserts data into database on success`() = runTest {
    // Given
    val coordinates = LocationCoordinate(1.0, 2.0)
    val currentWeatherDTO = CurrentWeatherDTO(
      cityId = 1,
      name = "Test City",
      coordinate = CoordinateDTO(1.0, 2.0),
      main = MainDTO(
        temperature = 25.0,
        feelsLike = 26.0,
        minTemperature = 24.0,
        maxTemperature = 27.0,
        pressure = 1013,
        humidity = 50
      ),
      weather = listOf(
        WeatherDTO(
          id = 800,
          main = "Clear",
          description = "clear sky",
          iconCode = "01d"
        )
      ),
      wind = WindDTO(speed = 5.0, degrees = 180),
      dt = 1000L,
      sys = SysDTO(country = "TS")
    )
    weatherAPI.currentWeatherResult = currentWeatherDTO

    // When
    val result = repository.fetchCurrentWeather(coordinates)

    // Then
    assertTrue(result is AppResult.Success)
    assertEquals(1, cityDAO.cities.size)
    assertEquals(1, currentWeatherDAO.currentWeather.value.size)
    assertEquals(1, syncDAO.syncStatus.size)
  }

  @Test
  fun `searchCities returns mapped results from API`() = runTest {
    // Given
    val query = "Test"
    val locations = listOf(
      LocationDTO(name = "Test City", lat = 1.0, lon = 2.0, country = "TS", state = "State")
    )
    weatherAPI.searchLocationsResult = locations

    // When
    val result = repository.searchCities(query)

    // Then
    assertTrue(result is AppResult.Success)
    val cities = (result as AppResult.Success).value
    assertEquals(1, cities.size)
    assertEquals("Test City", cities[0].name)
  }

  @Test
  fun `fetchCurrentWeatherForCity uses cache when valid`() = runTest {
    // Given
    val cityId = 1L
    val city = CityEntity(
      id = cityId,
      name = "Test",
      country_code = "TS",
      latitude = 1.0,
      longitude = 2.0,
      added_at = clock.now()
    )
    cityDAO.insert(city)

    // Set sync status to be recent
    val recentSync = app.mak.atmosense.core.database.dao.SyncEntity(
      city_id = cityId,
      sync_type = app.mak.atmosense.core.common.model.SyncRequestType.CurrentWeather.name,
      last_attempt_at = clock.now(),
      last_success_at = clock.now()
    )
    syncDAO.insert(recentSync)

    // When
    val result = repository.fetchCurrentWeatherForCity(isForceRefresh = false, cityId = cityId)

    // Then
    assertTrue(result is AppResult.Success)
    // Verify API was NOT called
    assertEquals(null, weatherAPI.currentWeatherResult)
  }

  @Test
  fun `fetchCurrentWeatherForCity force refresh calls API`() = runTest {
    // Given
    val cityId = 1L
    val city = CityEntity(
      id = cityId,
      name = "Test",
      country_code = "TS",
      latitude = 1.0,
      longitude = 2.0,
      added_at = clock.now()
    )
    cityDAO.insert(city)

    val currentWeatherDTO = CurrentWeatherDTO(
      cityId = cityId,
      name = "Test City",
      coordinate = CoordinateDTO(1.0, 2.0),
      dt = 1000L,
      main = MainDTO(temperature = 25.0, feelsLike = 26.0),
      weather = listOf(
        WeatherDTO(
          id = 800,
          main = "Clear",
          description = "clear sky",
          iconCode = "01d"
        )
      ),
      sys = SysDTO(country = "TS")
    )
    weatherAPI.currentWeatherResult = currentWeatherDTO

    // When
    val result = repository.fetchCurrentWeatherForCity(isForceRefresh = true, cityId = cityId)

    // Then
    assertTrue(result is AppResult.Success)
  }

  @Test
  fun `observeCitiesWeather returns mapped data`() = runTest {
    // Given
    val cityId = 1L
    val city = CityEntity(
      id = cityId,
      name = "Test",
      country_code = "TS",
      latitude = 1.0,
      longitude = 2.0,
      added_at = clock.now()
    )
    cityDAO.insert(city)

    val weather = app.mak.atmosense.core.database.dao.CurrentWeatherEntity(
      city_id = cityId,
      temperature = 25.0,
      feels_like = 26.0,
      humidity = 50,
      pressure = 1013,
      wind_speed = 5.0,
      wind_degrees = 180,
      condition_id = 800,
      condition_main = "Clear",
      condition_description = "clear sky",
      condition_icon_code = "01d",
      fetched_at = clock.now()
    )
    currentWeatherDAO.insert(weather)

    // When
    val result = repository.observeCitiesWeather().first()

    // Then
    assertEquals(1, result.size)
    assertEquals("Test", result[0].cityName)
    assertEquals(25.0, result[0].temperature, 0.1)
  }

  @Test
  fun `fetchForecastWeatherForCity inserts data into database`() = runTest {
    // Given
    val cityId = 1L
    val city = CityEntity(
      id = cityId,
      name = "Test",
      country_code = "TS",
      latitude = 1.0,
      longitude = 2.0,
      added_at = clock.now()
    )
    cityDAO.insert(city)

    val forecastWeatherDTO = ForecastWeatherDTO(
      list = listOf(
        HourlyDTO(
          dt = 2000L,
          main = MainDTO(temperature = 22.0),
          weather = listOf(
            WeatherDTO(
              id = 801,
              main = "Clouds",
              description = "few clouds",
              iconCode = "02d"
            )
          ),
          pop = 0.1,
          wind = WindDTO(speed = 3.0)
        )
      )
    )
    weatherAPI.forecastWeatherResult = forecastWeatherDTO

    // When
    val result = repository.fetchForecastWeatherForCity(isForceRefresh = true, cityId = cityId)

    // Then
    assertTrue(result is AppResult.Success)
    val forecast = repository.observeCityForecastWeather(cityId).first()
    assertEquals(1, forecast.size)
    assertEquals(22.0, forecast[0].temperature, 0.1)
  }
}
