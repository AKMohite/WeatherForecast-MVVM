package app.mak.atmosense.core.domain.usecase

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.domain.repository.WeatherRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.cancellation.CancellationException

@Inject
class RefreshWeatherDetails(
  private val weatherRepository: WeatherRepository,
) {
  suspend operator fun invoke(
    isForceRefresh: Boolean = false,
    cityId: Long
  ): AppResult<Unit> = supervisorScope {
    try {
      val currentResultDeferred = async {
        weatherRepository.fetchCurrentWeatherForCity(isForceRefresh, cityId)
      }
      val forecastResultDeferred = async {
        weatherRepository.fetchForecastWeatherForCity(isForceRefresh, cityId)
      }

      val currentResult = currentResultDeferred.await()
      val forecastResult = forecastResultDeferred.await()

      if (currentResult is AppResult.Failure) return@supervisorScope currentResult
      if (forecastResult is AppResult.Failure) return@supervisorScope forecastResult

      AppResult.Success(Unit)
    } catch (e: AppException) {
      AppResult.Failure(error = e.error, exception = e)
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      val appError = AppError.Unknown(null, e.message)
      AppResult.Failure(error = appError, exception = AppException(appError, e))
    }
  }
}
