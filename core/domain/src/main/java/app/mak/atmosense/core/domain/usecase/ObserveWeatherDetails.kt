package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.WeatherDetails
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@Inject
class ObserveWeatherDetails(
  private val weatherRepository: WeatherRepository
) {
  operator fun invoke(
    cityId: Long
  ) = combine(
    observeCurrentWeather(cityId),
    observeForecastWeather(cityId)
  ) { current, forecast ->
    WeatherDetails(
      currentWeather = current,
      forecastWeather = forecast
    )
  }

  private fun observeForecastWeather(cityId: Long): Flow<List<ForecastSlot>> =
    weatherRepository.observeCityForecastWeather(cityId)

  private fun observeCurrentWeather(cityId: Long): Flow<CityWeather> =
    weatherRepository.observeCityCurrentWeather(cityId)
      .filterNotNull()
}
