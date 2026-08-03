package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Inject
class RefreshWeatherDetails(
  private val weatherRepository: WeatherRepository,
) {
  suspend operator fun invoke(
    cityId: Long
  ) = supervisorScope {
    launch {
      weatherRepository.fetchCurrentWeatherForCity(cityId)
    }
    launch {
      weatherRepository.fetchForecastWeatherForCity(cityId)
    }
  }
}
