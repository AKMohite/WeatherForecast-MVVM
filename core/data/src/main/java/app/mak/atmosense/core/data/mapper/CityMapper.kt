package app.mak.atmosense.core.data.mapper

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import app.mak.atmosense.core.database.dao.CityEntity
import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant

internal fun CurrentWeatherDTO.toCityEntity(now: Instant): CityEntity = CityEntity(
  id = cityId,
  name = name.orEmpty(),
  latitude = coordinate?.lat ?: 0.0,
  longitude = coordinate?.lon ?: 0.0,
  country_code = sys?.country.orEmpty(),
  added_at = now
)

fun Throwable.toAppException(): AppException {
  return when (this) {
    is CancellationException -> throw this
    is AppException -> this
    else -> AppException(AppError.Unknown(code = null, message = this.message), this)
  }
}
