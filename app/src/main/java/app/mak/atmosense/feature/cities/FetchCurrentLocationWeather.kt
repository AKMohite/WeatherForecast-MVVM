package app.mak.atmosense.feature.cities

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
    val result = locationCoordinator.resolveCurrentLocation()
    if (result is LocationAccessResult.Available) {
      weatherRepository.fetchCurrentWeather(result.coordinates)
    }
  }
}
