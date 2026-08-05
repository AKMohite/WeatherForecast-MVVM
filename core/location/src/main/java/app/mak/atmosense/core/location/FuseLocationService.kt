package app.mak.atmosense.core.location

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

interface LocationService {
  suspend fun getCurrentLocation(): AppResult<LocationCoordinate>
}

@SingleIn(AppScope::class)
@ContributesBinding(scope = AppScope::class)
@Inject
class FuseLocationService(
  private val fusedClient: FusedLocationProviderClient,
) : LocationService {

  //  @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
  override suspend fun getCurrentLocation(): AppResult<LocationCoordinate> {
    return try {
      val request = CurrentLocationRequest.Builder()
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()
      val fresh = withTimeoutOrNull(FRESH_FETCH_TIMEOUT_MS.milliseconds) {
        fusedClient.getCurrentLocation(request, null).await()
      }
      val location = fresh ?: fusedClient.lastLocation.await()
      if (location != null) {
        AppResult.Success(
          LocationCoordinate(
            latitude = location.latitude,
            longitude = location.longitude,
            isCurrentLocation = true
          )
        )
      } else {
        AppResult.Failure(AppError.Unknown(code = null, message = "No location available"))
      }
    } catch (e: SecurityException) {
      // Should be unreachable if callers honor LocationPermissionChecker first, but Play
      // Services throws this at the API boundary regardless — don't let it crash the caller.
      AppResult.Failure(AppError.Unknown(code = null, message = "Missing location permission"))
    } catch (e: Exception) {
      if (e is CancellationException) throw e
      AppResult.Failure(AppError.Unknown(code = null, message = e.message))
    }
  }

  private companion object {
    const val FRESH_FETCH_TIMEOUT_MS = 10_000L
  }
}

