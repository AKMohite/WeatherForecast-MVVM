package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

@Inject
class RefreshWeatherDetails(
  private val weatherRepository: WeatherRepository,
) {
  suspend operator fun invoke(
    isForceRefresh: Boolean = false,
    cityId: Long
  ) = supervisorScope {
    val currentResult = async {
      weatherRepository.fetchCurrentWeatherForCity(isForceRefresh, cityId)
    }
    val forecastResult = async {
      weatherRepository.fetchForecastWeatherForCity(isForceRefresh, cityId)
    }
    WeatherDetailsResult(
      currentWeather = currentResult.await(),
      forecastWeather = forecastResult.await()
    )
  }
}

data class WeatherDetailsResult(
  val currentWeather: AppResult<Unit>,
  val forecastWeather: AppResult<Unit>
)
