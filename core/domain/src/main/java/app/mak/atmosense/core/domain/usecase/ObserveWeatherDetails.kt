package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.UserSettings
import app.mak.atmosense.core.common.model.WeatherDetails
import app.mak.atmosense.core.common.util.UnitConverter
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@Inject
class ObserveWeatherDetails(
  private val weatherRepository: WeatherRepository,
  private val observeUserSettings: ObserveUserSettings
) {
  operator fun invoke(
    cityId: Long
  ) = combine(
    observeCurrentWeather(cityId),
    observeForecastWeather(cityId),
    observeUserSettings()
  ) { current, forecast, settings ->
    WeatherDetails(
      currentWeather = convertCityWeather(current, settings),
      forecastWeather = forecast.map { convertForecastSlot(it, settings) }
    )
  }

  private fun convertCityWeather(weather: CityWeather, settings: UserSettings): CityWeather {
    return weather.copy(
      temperature = UnitConverter.convertTemperature(weather.temperature, settings.temperatureUnit),
      feelsLike = UnitConverter.convertTemperature(weather.feelsLike, settings.temperatureUnit),
      pressure = UnitConverter.convertPressure(weather.pressure.toLong(), settings.pressureUnit),
      windSpeed = UnitConverter.convertWindSpeed(weather.windSpeed, settings.windSpeedUnit)
    )
  }

  private fun convertForecastSlot(slot: ForecastSlot, settings: UserSettings): ForecastSlot {
    return slot.copy(
      temperature = UnitConverter.convertTemperature(slot.temperature, settings.temperatureUnit),
      windSpeed = UnitConverter.convertWindSpeed(slot.windSpeed, settings.windSpeedUnit)
    )
  }

  private fun observeForecastWeather(cityId: Long): Flow<List<ForecastSlot>> =
    weatherRepository.observeCityForecastWeather(cityId)

  private fun observeCurrentWeather(cityId: Long): Flow<CityWeather> =
    weatherRepository.observeCityCurrentWeather(cityId)
      .filterNotNull()
}
