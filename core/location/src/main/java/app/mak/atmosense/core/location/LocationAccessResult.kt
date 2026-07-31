package app.mak.atmosense.core.location

import app.mak.atmosense.core.common.model.AppError

sealed interface LocationAccessResult {
  data class Available(val coordinates: Coordinates) : LocationAccessResult

  //  data object PermissionDenied: LocationAccessResult
  data class Unavailable(val error: AppError) : LocationAccessResult
}

