package app.mak.atmosense.core.location

import app.mak.atmosense.core.common.model.AppResult
import dev.zacsweers.metro.Inject

@Inject
class LocationAccessCoordinator(
  private val locationService: LocationService,
) {

  suspend fun resolveCurrentLocation(): LocationAccessResult {
    return when (val result = locationService.getCurrentLocation()) {
      is AppResult.Success -> LocationAccessResult.Available(result.value)
      is AppResult.Failure -> LocationAccessResult.Unavailable(result.error)
    }
  }

}
