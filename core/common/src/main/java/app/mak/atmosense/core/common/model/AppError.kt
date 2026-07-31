package app.mak.atmosense.core.common.model

sealed interface AppError {
  data object NoInternet : AppError
  data class Unknown(val code: Int?, val message: String?) : AppError
}
