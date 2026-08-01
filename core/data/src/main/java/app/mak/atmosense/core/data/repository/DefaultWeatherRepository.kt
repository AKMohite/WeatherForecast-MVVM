package app.mak.atmosense.core.data.repository

import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.domain.repository.WeatherRepository
import app.mak.atmosense.core.network.WeatherAPI
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlin.coroutines.cancellation.CancellationException

@ContributesBinding(scope = AppScope::class)
@Inject
class DefaultWeatherRepository(
  private val weatherAPI: WeatherAPI
) : WeatherRepository {

  override suspend fun fetchCurrentWeather(coordinates: LocationCoordinate) {
    try {
      val queries = mapOf(
        "lat" to coordinates.latitude.toString(),
        "lon" to coordinates.longitude.toString(),
        "unit" to "metric"
      )
      weatherAPI.getCurrentWeather(queries)
    } catch (t: Throwable) {
      if (t is CancellationException) throw t
    }
  }
}
