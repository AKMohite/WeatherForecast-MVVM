package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDTO(
  @SerialName("coord")
  val coord: CoordinateDTO? = null,
  @SerialName("country")
  val country: String? = null,
  @SerialName("id")
  val id: Int? = null,
  @SerialName("name")
  val name: String? = null,
  @SerialName("population")
  val population: Int? = null,
  @SerialName("sunrise")
  val sunrise: Int? = null,
  @SerialName("sunset")
  val sunset: Int? = null,
  @SerialName("timezone")
  val timezone: Int? = null
)
