package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainDTO(
  @SerialName("feels_like")
  val feelsLike: Double? = null,
  @SerialName("grnd_level")
  val groundLevel: Int? = null,
  @SerialName("humidity")
  val humidity: Int? = null,
  @SerialName("pressure")
  val pressure: Int? = null,
  @SerialName("sea_level")
  val seaLevel: Int? = null,
  @SerialName("temp")
  val temperature: Double? = null,
  @SerialName("temp_max")
  val maxTemperature: Double? = null,
  @SerialName("temp_min")
  val minTemperature: Double? = null
)
