package app.mak.atmosense.core.common.model

data class WeatherCondition(
  val id: Long,
  val main: String,
  val description: String,
  val iconCode: String,
)
