package app.mak.atmosense.core.sync

import app.mak.atmosense.core.database.dao.api.CityDAO
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
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
              refreshWeatherDetails(city.id, true)
            } catch (e: Exception) {
              if (e is CancellationException) throw e
            }
          }
        }
      }
    }
  }

  private suspend fun refreshWeatherDetails(id: Long, isForceRefresh: Boolean) {
    weatherRepository.fetchCurrentWeatherForCity(isForceRefresh, id)
    weatherRepository.fetchForecastWeatherForCity(isForceRefresh, id)
  }

}
