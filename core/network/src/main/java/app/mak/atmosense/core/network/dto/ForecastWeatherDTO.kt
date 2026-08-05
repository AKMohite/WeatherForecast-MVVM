package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeatherDTO(
  @SerialName("city")
  val city: CityDTO? = null,
  @SerialName("cnt")
  val cnt: Int? = null,
  @SerialName("cod")
  val cod: String? = null,
  @SerialName("list")
  val list: List<HourlyDTO>? = null,
  @SerialName("message")
  val message: Int? = null
)
