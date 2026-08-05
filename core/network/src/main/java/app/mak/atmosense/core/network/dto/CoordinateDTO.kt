package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoordinateDTO(
  @SerialName("lat")
  val lat: Double? = null,
  @SerialName("lon")
  val lon: Double? = null
)
