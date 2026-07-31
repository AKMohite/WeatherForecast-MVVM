package app.mak.atmosense.core.location

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.LocationCoordinate

sealed interface LocationAccessResult {
  data class Available(val coordinates: LocationCoordinate) : LocationAccessResult

  //  data object PermissionDenied: LocationAccessResult
  data class Unavailable(val error: AppError) : LocationAccessResult
}

