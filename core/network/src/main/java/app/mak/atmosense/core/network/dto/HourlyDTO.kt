package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyDTO(
  @SerialName("clouds")
  val clouds: CloudsDTO? = null,
  @SerialName("dt")
  val dt: Long? = null,
  @SerialName("dt_txt")
  val dtTxt: String? = null,
  @SerialName("main")
  val main: MainDTO? = null,
  @SerialName("pop")
  val pop: Double? = null,
  @SerialName("rain")
  val rain: RainDTO? = null,
  @SerialName("sys")
  val sys: SysDTO? = null,
  @SerialName("visibility")
  val visibility: Int? = null,
  @SerialName("weather")
  val weather: List<WeatherDTO>? = null,
  @SerialName("wind")
  val wind: WindDTO? = null
)
