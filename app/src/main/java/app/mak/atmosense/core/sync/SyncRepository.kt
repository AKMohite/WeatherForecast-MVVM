package app.mak.atmosense.core.sync

import android.util.Log
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.database.dao.api.CityDAO
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

interface SyncRepository {
  suspend fun sync()

}

@ContributesBinding(AppScope::class)
@Inject
class DefaultSyncRepository(
  private val weatherRepository: WeatherRepository,
  private val cityDAO: CityDAO
) : SyncRepository {
  override suspend fun sync() {
    withContext(Dispatchers.IO) {
      // TODO need to update all cities?
      val cities = cityDAO.getAll().take(10)

      supervisorScope {
        cities.forEachIndexed { index, city ->
          launch {
            try {
              // Stagger the starts slightly (500ms) to avoid burst API calls
              // while still being faster than pure sequential execution.
              delay((index * 500).milliseconds)
              val (currentResult, forecastResult) = refreshWeatherDetails(city.id, true)

              if (currentResult is AppResult.Failure) {
                Log.e(
                  "SyncRepository",
                  "Failed to sync current weather for city ${city.id}: ${currentResult.error}"
                )
              }
              if (forecastResult is AppResult.Failure) {
                Log.e(
                  "SyncRepository",
                  "Failed to sync forecast for city ${city.id}: ${forecastResult.error}"
                )
              }
            } catch (e: Exception) {
              if (e is CancellationException) throw e
              Log.e("SyncRepository", "Error syncing city ${city.id}", e)
            }
          }
        }
      }
    }
  }

  private suspend fun refreshWeatherDetails(id: Long, isForceRefresh: Boolean) = coroutineScope {
    val current = async { weatherRepository.fetchCurrentWeatherForCity(isForceRefresh, id) }
    val forecast = async { weatherRepository.fetchForecastWeatherForCity(isForceRefresh, id) }
    Pair(current.await(), forecast.await())
  }

}
