package app.mak.atmosense.core.common.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

data class ForecastSlot(
  val cityId: Long,
  val timestamp: Instant,
  val temperature: Double,
  val condition: WeatherCondition?,
  val precipitationProbability: Double,
  val windSpeed: Double,
) {
  val hour = "${timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).time}"
  val image = "https://openweathermap.org/img/wn/${condition?.iconCode}@2x.png"
}
