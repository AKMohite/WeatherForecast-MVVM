package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
  @SerialName("country")
  val country: String? = null,
  @SerialName("lat")
  val lat: Double? = null,
  @SerialName("lon")
  val lon: Double? = null,
  @SerialName("name")
  val name: String? = null,
  @SerialName("state")
  val state: String? = null
)
