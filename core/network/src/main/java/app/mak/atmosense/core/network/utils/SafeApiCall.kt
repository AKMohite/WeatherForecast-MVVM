package app.mak.atmosense.core.network.utils

import app.mak.atmosense.core.common.model.AppError
import app.mak.atmosense.core.common.model.AppException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

internal suspend inline fun <reified T> safeApiCall(
  block: suspend () -> T
): T {
  return try {
    block()
  } catch (t: Throwable) {
    val error = when (t) {
      is CancellationException, is AppException -> throw t
      is HttpRequestTimeoutException -> AppError.Timeout
      is IOException -> AppError.NoInternet
      else -> AppError.Unknown(null, t.message)
    }
    throw AppException(error, t)
  }
}
