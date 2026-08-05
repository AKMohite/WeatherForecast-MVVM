package app.mak.atmosense.core.common.model

sealed interface AppError {
  data object NoInternet : AppError
  data object Timeout : AppError
  data object EntityNotFound : AppError
  data class Unknown(val code: Int?, val message: String? = null) : AppError
}

data class AppException(
  val error: AppError,
  override val cause: Throwable? = null
) : Throwable("Exception occurred with error: $error and cause: $cause")
