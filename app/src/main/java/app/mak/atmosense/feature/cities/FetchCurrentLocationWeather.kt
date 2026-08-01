package app.mak.atmosense.feature.cities

import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.location.LocationAccessCoordinator
import app.mak.atmosense.core.location.LocationAccessResult
import dev.zacsweers.metro.Inject

@Inject
class FetchCurrentLocationWeather(
  private val locationCoordinator: LocationAccessCoordinator,
  private val weatherRepository: WeatherRepository
) {
  suspend operator fun invoke() {
    val locationResult = locationCoordinator.resolveCurrentLocation()
    if (locationResult is LocationAccessResult.Available) {
      when (val weatherResult = weatherRepository.fetchCurrentWeather(locationResult.coordinates)) {
        is AppResult.Success -> {
          // Handle success
        }

        is AppResult.Failure -> {
          // Handle failure
        }
      }
    }
  }
}
