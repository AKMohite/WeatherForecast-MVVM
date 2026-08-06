package app.mak.atmosense.core.location.testing

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppResult
import app.mak.atmosense.core.common.model.LocationCoordinate
import app.mak.atmosense.core.location.LocationService

class FakeLocationService : LocationService {
  var result: AppResult<LocationCoordinate> = AppResult.Failure(AppError.Unknown(null, null))
  override suspend fun getCurrentLocation(): AppResult<LocationCoordinate> = result
}
