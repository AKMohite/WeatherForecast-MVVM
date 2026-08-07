package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.TemperatureUnit
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.domain.testing.FakeSettingsRepository
import app.mak.atmosense.core.domain.testing.FakeWeatherRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveWeatherDetailsTest {

  private val weatherRepository = FakeWeatherRepository()
  private val settingsRepository = FakeSettingsRepository()
  private val observeUserSettings = ObserveUserSettings(settingsRepository)
  private val useCase = ObserveWeatherDetails(weatherRepository, observeUserSettings)

  @Test
  fun `weather data is converted based on user settings`() = runTest {
    // given
    val cityId = 1L
    val baseTemp = 300.0 // Kelvin
    val baseWind = 10.0 // m/s

    weatherRepository.currentWeather = CityWeather(
      cityId = cityId,
      cityName = "Test",
      countryCode = "TS",
      temperature = baseTemp,
      feelsLike = baseTemp,
      humidity = 50,
      pressure = 1013.0,
      windSpeed = baseWind,
      windDegrees = 180,
      weatherIcon = "",
      weatherDescription = "",
      fetchedBefore = ""
    )

    // when: user settings are Celsius
    settingsRepository.updateUserSettings(UserSettings(temperatureUnit = TemperatureUnit.CELSIUS))
    val result = useCase(cityId).first()

    // then
    assertEquals(26.85, result.currentWeather.temperature, 0.01)

    // when: user settings are Fahrenheit
    settingsRepository.updateUserSettings(UserSettings(temperatureUnit = TemperatureUnit.FAHRENHEIT))
    val result2 = useCase(cityId).first()

    // then
    assertEquals(80.33, result2.currentWeather.temperature, 0.01)
  }
}
