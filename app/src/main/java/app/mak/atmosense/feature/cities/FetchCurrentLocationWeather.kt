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
  suspend operator fun invoke(): AppResult<Unit> {
    val locationResult = locationCoordinator.resolveCurrentLocation()
    return if (locationResult is LocationAccessResult.Available) {
      weatherRepository.fetchCurrentWeather(locationResult.coordinates)
    } else {
      val error = (locationResult as? LocationAccessResult.Unavailable)?.error
      AppResult.Failure(error = error)
    }
  }
}
