package app.mak.atmosense.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RainDTO(
  @SerialName("1h")
  val oneHour: Double? = null,
  @SerialName("3h")
  val threeHour: Double? = null
)
