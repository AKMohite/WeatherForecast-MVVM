package app.mak.atmosense.core.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDTO(
  @SerialName("base")
  val base: String? = null,
  @SerialName("clouds")
  val clouds: CloudsDTO? = null,
  @SerialName("cod")
  val cod: Int? = null,
  @SerialName("coord")
  val coordinate: CoordinateDTO? = null,
  @SerialName("dt")
  val dt: Long? = null,
  @SerialName("id")
  val cityId: Long,
  @SerialName("main")
  val main: MainDTO? = null,
  @SerialName("name")
  val name: String? = null,
  @SerialName("sys")
  val sys: SysDTO? = null,
  @SerialName("timezone")
  val timezone: Int? = null,
  @SerialName("visibility")
  val visibility: Int? = null,
  @SerialName("weather")
  val weather: List<WeatherDTO>? = null,
  @SerialName("wind")
  val wind: WindDTO? = null
)
